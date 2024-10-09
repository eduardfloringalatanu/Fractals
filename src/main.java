import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class main {
	static final int IMAGE_WIDTH = 1024;
	static final int IMAGE_HEIGHT = 768;
	static final float IMAGE_WIDTH2 = IMAGE_WIDTH / 2;
	static final float IMAGE_HEIGHT2 = IMAGE_HEIGHT / 2;
	
	static final int MAX_ITERATIONS = 500;
	
	static BufferedImage bufferedimage;
	
	static int image_scale = 200;
	static float center_x = 0.0f;
	static float center_y = 0.0f;

	static int mandelbrot_set(ComplexNumber c0) {
		ComplexNumber c = c0;
		
		for (int i = 0; i < MAX_ITERATIONS; ++i) {
			if (c.absolute() > 2.0f)
				return Color.HSBtoRGB((float)i / MAX_ITERATIONS, 1.0f, 1.0f);
			
			c = c.power(2).add(c0);
		}
		
		return Color.HSBtoRGB(0.0f, 0.0f, 0.0f); // black
	}
	
	static int tricorn(ComplexNumber c0) {
		ComplexNumber c = c0;
		
		for (int i = 0; i < MAX_ITERATIONS; ++i) {
			if (c.absolute() > 2.0f)
				return Color.HSBtoRGB((float)i / MAX_ITERATIONS, 1.0f, 1.0f);
			
			c = c.conjugate().power(2).add(c0);
		}
		
		return Color.HSBtoRGB(0.0f, 0.0f, 0.0f); // black
	}
	
	static int newton_fractal(ComplexNumber c0) {
		ComplexNumber c = c0;
		
		int i, j;
		
		ComplexNumber p, p_derivative, difference;
		ComplexNumber roots[] =
			{
				new ComplexNumber(1.0f, 0.0f),
				new ComplexNumber(-0.5f, (float)Math.sqrt(3) / 2),
				new ComplexNumber(-0.5f, (float)Math.sqrt(3) / -2)
			};
		
		final int colors[] =
			{
				Color.HSBtoRGB(0.0f, 1.0f, 1.0f),
				Color.HSBtoRGB(0.3f, 1.0f, 1.0f),
				Color.HSBtoRGB(0.6f, 1.0f, 1.0f)
			};
		
		final ComplexNumber z1 = new ComplexNumber(1.0f, 0.0f);
		final ComplexNumber z2 = new ComplexNumber(3.0f, 0.0f);
		
		for (i = 0; i < MAX_ITERATIONS; ++i) {
			for (j = 0; j < roots.length; ++j) {
				difference = c.subtract(roots[j]);
				
				if (difference.absolute() < 0.000001f)
					return colors[j];
			}
			
			p = c.power(3).subtract(z1);
			p_derivative = c.power(2).multiply(z2);
			c = c.subtract(p.divide(p_derivative));
		}
		
		return Color.HSBtoRGB(0.0f, 0.0f, 0.0f); // black
	}
	
	static void draw(int fractal) {
		int x, y;
		
		ComplexNumber c0;
		
		for (x = 0; x < IMAGE_WIDTH; ++x) {
			for (y = 0; y < IMAGE_HEIGHT; ++y) {
				c0 = new ComplexNumber((x - IMAGE_WIDTH2) / image_scale + center_x, (y - IMAGE_HEIGHT2) / image_scale + center_y);
				
				switch (fractal) {
				case 0:
					bufferedimage.setRGB(x, y, mandelbrot_set(c0));
					
					break;
				case 1:					
					bufferedimage.setRGB(x, y, tricorn(c0));
					
					break;
				case 2:					
					bufferedimage.setRGB(x, y, newton_fractal(c0));
					
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		final JFrame jframe = new JFrame();
		
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		jframe.getContentPane().setLayout(new BorderLayout());
		
		final JComboBox jcombobox = new JComboBox(new String[]
				{
					"Mandelbrot set",
					"Tricorn",
					"Newton fractal"
				});
		
		final class ImageJPanel extends JPanel {
			@Override
			public void paintComponent(Graphics g) {		
				g.drawImage(bufferedimage, 0, 0, null);
			}
		}
		
		final ImageJPanel imagejpanel = new ImageJPanel();
		
		jcombobox.setFont(new Font("Verdana", Font.PLAIN, 18));
		jcombobox.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jcombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_scale = 200;
				center_x = 0.0f;
				center_y = 0.0f;
				
				long start = System.currentTimeMillis();
						
				draw(jcombobox.getSelectedIndex());
				
				long finish = System.currentTimeMillis();
				
				jframe.setTitle("Fractals | Drawing time: " + (finish - start) + " milliseconds");
				
				imagejpanel.repaint();
			}
		});
		
		imagejpanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imagejpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		imagejpanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int x = e.getX();
            	int y = e.getY();
            	
            	if (e.getButton() == MouseEvent.BUTTON1) {
            		center_x += (x - IMAGE_WIDTH2) / image_scale;
                	center_y += (y - IMAGE_HEIGHT2) / image_scale;
            		
            		image_scale *= 2;
            	}
            	else if (e.getButton() == MouseEvent.BUTTON3) {
            		center_x += (x - IMAGE_WIDTH2) / image_scale;
                	center_y += (y - IMAGE_HEIGHT2) / image_scale;
            		
            		image_scale /= 2;
            	}
            	else
            		return;
            	
            	long start = System.currentTimeMillis();
				
				draw(jcombobox.getSelectedIndex());
				
				long finish = System.currentTimeMillis();
				
				jframe.setTitle("Fractals | Drawing time: " + (finish - start) + " milliseconds");
            	
            	imagejpanel.repaint();
            }
        });
		
		jframe.add(jcombobox, BorderLayout.NORTH);
		jframe.add(imagejpanel, BorderLayout.CENTER);
		
		bufferedimage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		long start = System.currentTimeMillis();
		
		draw(jcombobox.getSelectedIndex());
		
		long finish = System.currentTimeMillis();
		
		jframe.setTitle("Fractals | Drawing time: " + (finish - start) + " milliseconds");
		
		jframe.pack();
		jframe.setVisible(true);
		jframe.setLocationRelativeTo(null);
	}
}
