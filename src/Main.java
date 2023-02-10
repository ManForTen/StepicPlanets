import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    static int width = 1400, height = 900;
    static JFrame frame = new JFrame();

    public static class RunPlanet extends Thread {
        private int sleep;
        CreatePlanet planet;
        public RunPlanet(CreatePlanet planet,int sleep) {
            this.planet = planet;
            this.sleep = sleep;
        }
        @Override
        public void run() {
            for (int t = 0; t < 360;) {
                try { // Таймер
                    Thread.sleep(sleep);
                    planet.l.setBounds((int) (planet.r*Math.cos(t*Math.PI/180))+planet.dX, (int) (planet.r*Math.sin(t*Math.PI/180))+planet.dY, planet.w, planet.h); // Перемещаем картинку в нужную точку, которую вычисляем по формуле параметрических уравнений линий, приводя градусы t к радианам
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                t = t==359 ? 0 : t+1; // Формула, которая обеспечивает постоянный цикл и обнуление t при 359
            }
        }
    }

    public static class CreatePlanet{
        private String path;
        public int w;
        public int h;
        public int r; // Радиус от центра
        public int dX;
        public int dY;
        public JLabel l;
        public CreatePlanet(String path, int r) throws IOException {
            this.path = path;
            this.r=r;
            BufferedImage im = ImageIO.read(new File("src//"+path));
            w = im.getWidth();
            h = im.getHeight();
            l = new JLabel(new ImageIcon(im));
            if (r==0) l.setBounds(width / 2 - w / 2, height / 2 - h / 2, w, h);
            dX=width/2-w/2;
            dY=height/2-h/2;
            frame.add(l);
        }
    }

    public static void main(String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Солнечная система");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.black);
        new CreatePlanet("sun.png",0);
        frame.setVisible(true);
        String[][] planets = new String[][]{new String []{"2","140","5"},{"3","160","10"},{"earth","190","15"},{"4","220","20"},
                {"5","270","30"},{"6","340","40"},{"7","410","60"},{"8","450","100"}};
        for (int i = 0; i < planets.length; i++)
            new RunPlanet(new CreatePlanet(planets[i][0]+".png",Integer.valueOf(planets[i][1])),Integer.valueOf(planets[i][2])).start();
    }
}