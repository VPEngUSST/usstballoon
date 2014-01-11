package usstv;

import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Encoder
{
	
	private Encoder() // no constructor, encoding is a static method
	{}
	
	// Split the image into a series of small jpegs and write to stdout
	public static void encodeImage(int rows, int cols) throws IOException
	{
		if(rows < 1 || cols < 1) // check argument sanity
		{
			System.err.println("Illegal argument.");
			return;
		}
		
		BufferedImage image = ImageIO.read(System.in); // the image to be sent, read from stdin
		int chunkWidth = image.getWidth()/cols; // width of each piece in px
		int chunkHeight = image.getHeight()/rows;

		for(int i = 0; i < rows; i++) // for each row
		{
			for(int j = 0; j < cols; j++) // for each column
			{
				BufferedImage chunk = image.getSubimage(j*chunkWidth, i*chunkHeight, chunkWidth, chunkHeight); // copy a piece from the image
				ImageIO.write(chunk, "jpg", System.out); // output the jpeg
				System.out.write(i/256); // write grid position information
				System.out.write(i%256);
				System.out.write(j/256);
				System.out.write(j%256);
			}
		}
		System.out.flush(); // make sure everything gets written
	}
	
	public static void main(String[] args)
	{
		if(args.length != 2) // check correct number of args
		{
			System.err.println("Wrong number of arguments.");
			return;
		}
		
		int rows = Integer.parseInt(args[0]); // get arguments from command line
		int cols = Integer.parseInt(args[1]);
		
		try
		{
			Encoder.encodeImage(rows, cols);
		}
		catch(IOException e) // report problems with i/o to stderr
		{
			System.err.println("Got IOException: " + e.getMessage());
		}
	}
}
