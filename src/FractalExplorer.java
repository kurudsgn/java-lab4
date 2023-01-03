import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
    private int size;
    private JImageDisplay image;
    private FractalGenerator gen;
    private Rectangle2D.Double range;

    public FractalExplorer(int s) {
        size = s;
        gen = new Mandelbrot();
        range = new Rectangle2D.Double();
        gen.getInitialRange(range);
        image = new JImageDisplay(size, size);
    }

    public void createAndShowGUI() {
        image.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");
        frame.add(image, BorderLayout.CENTER);
        JButton reset = new JButton("reset");

        ButtonClick resetHandler = new ButtonClick();
        reset.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        image.addMouseListener(click);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel myPanel = new JPanel();

        frame.add(myPanel, BorderLayout.NORTH);

        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(reset);
        frame.add(myBottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double xCoord = gen.getCoord(range.x,
                        range.x + range.width, size, x);
                double yCoord = gen.getCoord(range.y,
                        range.y + range.height, size, y);

                int numIters = gen.numIterations(xCoord, yCoord);
                if (numIters == -1) {
                    image.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    image.drawPixel(x, y, rgbColor);
                }
            }
            image.repaint();

        }
    }

    private class ButtonClick implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();


            if (command.equals("Reset")) {
                gen.getInitialRange(range);
                drawFractal();
            }

        }
    }
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            double xCoord = gen.getCoord(range.x,
                    range.x + range.width, size, x);

            int y = e.getY();
            double yCoord = gen.getCoord(range.y, range.y + range.height, size, y);

            gen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }
}

