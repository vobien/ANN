package vn.tribt.ai.image.process;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageExtraction {

    public static final String IMAGE_PATH = "resources/numbers.jpg";

    private List<LineCharacter> lines;

    private Mat binaryImg;

    public ImageExtraction() {
        lines = new ArrayList<>();
        new File("number").mkdir();
    }

    public static void main(String[] args) throws IOException {
        // Important! load native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ImageExtraction imgExtraction = new ImageExtraction();
        imgExtraction.convertColor2BinaryImg(IMAGE_PATH);

        imgExtraction.printMatrix();

        imgExtraction.getCharacterLines();

        imgExtraction.writeLines();

        imgExtraction.getCharacters();

        imgExtraction.printResult2File();

        imgExtraction.writeAllCharacters();

        System.out.println(imgExtraction);

    }

    public void getCharacterLines() {
        int startRow = 0;
        while (startRow < binaryImg.rows()) {
            int top = detectTopLine(startRow);
            int bottom = detectBottomLine(top + 1);
            LineCharacter line = new LineCharacter(top, bottom);
            lines.add(line);

            startRow = bottom + 1;
        }
    }

    private int detectTopLine(int start) {
        int row = start;
        while (row < binaryImg.rows()) {
            for (int col = 0; col < binaryImg.cols(); col++) {
                if (binaryImg.get(row, col)[0] == 0) {
                    return row;
                }
            }

            row++;
        }

        return row;
    }

    private int detectBottomLine(int top) {
        int row = top;
        int bottom = row;

        while (row < binaryImg.rows()) {
            int col = 0;
            for (col = 0; col < binaryImg.cols(); col++) {
                if (binaryImg.get(row, col)[0] == 0) {
                    bottom++;
                    break;
                }
            }

            if (col == binaryImg.cols()) {
                bottom = row - 1;
                break;
            }

            row++;
        }

        return bottom;
    }

    public void getCharacters() {
        for (LineCharacter line : lines) {
            getCharacterEachLine(line);
        }
    }

    public void getCharacterEachLine(LineCharacter line) {

        int col = 0;
        while (col < binaryImg.cols()) {
            int left = detectLeftBorder(col, line);
            int right = detectRightBorder(left + 1, line);
            int top = detectTopLine(line.getTop());
            int bottom = detectBottomLine(top + 1);

            if (right - left > 10) {
                Character character = new Character(top, bottom, left, right);
                line.addCharacter(character);
            }

            col = right + 1;
        }

    }

    private int detectLeftBorder(int start, LineCharacter line) {
        int col = start;
        while (col < binaryImg.cols()) {

            for (int i = line.getTop(); i < line.getBottom(); i++) {
                if (binaryImg.get(i, col)[0] == 0) {
                    return col;
                }
            }

            col++;
        }

        return col;
    }

    private int detectRightBorder(int start, LineCharacter line) {
        int col = start;

        while (col < binaryImg.cols()) {
            int row = line.getTop();
            for (row = line.getTop(); row < line.getBottom(); row++) {
                if (binaryImg.get(row, col)[0] == 0) {
                    break;
                }
            }

            if (row == line.getBottom()) {
                return col;
            }

            col++;
        }

        return col;
    }

    public void downSampling(int width, int height) {
        // TODO: down sampling image to 5x7
    }

    public void convertColor2BinaryImg(String imagePath) {
        Mat inImage = Imgcodecs.imread(imagePath);

        // convert RBG image to Gray image
        Mat grayImg = new Mat();
        Imgproc.cvtColor(inImage, grayImg, Imgproc.COLOR_RGB2GRAY);
        // Imgcodecs.imwrite("resources/X-gray.jpg", grayImg);

        // convert Gray to binary
        Mat binaryImg = new Mat();
        // thresh: the value is used to compare in the matrix
        // 0: black; 255: white
        Imgproc.threshold(grayImg, binaryImg, 200, 255, Imgproc.THRESH_BINARY);
        // Imgcodecs.imwrite("resources/X-binary.jpg", binaryImg);

        this.binaryImg = binaryImg;
    }

    private void printMatrix() throws IOException {
        FileWriter writer = new FileWriter(new File("number/matrix.txt"));
        for (int i = 0; i < binaryImg.rows(); i++) {
            for (int j = 0; j < binaryImg.cols(); j++) {
                writer.write(Arrays.toString(binaryImg.get(i, j)));
                writer.write("\t");
            }
            writer.write("\n");
        }
        writer.close();
    }

    @Override
    public String toString() {
        return "ImageExtraction [lines=" + lines + "]";
    }

    public Mat getBinaryImg() {
        return binaryImg;
    }

    public List<LineCharacter> getLines() {
        return lines;
    }

    public void writeLines() {
        for (int i = 0; i < lines.size(); i++) {
            writeLine(lines.get(i).getTop(), lines.get(i).getBottom(),
                    "number/line-" + i + ".jpg");
        }
    }

    public void writeAllCharacters() {
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).getCharacters().size(); j++) {
                Character c = lines.get(i).getCharacters().get(j);
                writeCharacter(c.getTop(), c.getBottom(), c.getLeft(), c
                        .getRight(), "number/character-" + i + "-" + j
                                + ".jpg");
            }
        }
    }

    public void writeLine(int top, int bottom, String filename) {
        Mat line = binaryImg.submat(top, bottom, 0, binaryImg.cols() - 1);
        Imgcodecs.imwrite(filename, line);
    }

    public void writeCharacter(int top, int bottom, int left, int right,
            String filename) {
        Mat character = binaryImg.submat(top, bottom, left, right);
        Imgcodecs.imwrite(filename, character);
    }

    public void printResult2File() throws IOException {
        FileWriter writer = new FileWriter(new File(
                "number/charater-extraction.txt"));
        for (LineCharacter line : lines) {
            writer.write(line.toString());
            writer.write("\n");
        }

        writer.close();
    }
}
