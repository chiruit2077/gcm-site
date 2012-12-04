package br.com.ecc.server.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	
	public static final Integer TAMANHO_IMAGEM = 1024;
	public static final Integer TAMANHO_THUMB = 150;


	public static byte[] scale(byte[] pImageData, int pMaxWidth) throws Exception {
		ImageIcon imageIcon = new ImageIcon(pImageData);
		int width = imageIcon.getIconWidth();
		int height = imageIcon.getIconHeight();
		if(width==-1 && height==-1){
			return null;
		}
		if (pMaxWidth > 0 && width > pMaxWidth) {
			double ratio = (double) pMaxWidth / imageIcon.getIconWidth();
			height = (int) (imageIcon.getIconHeight() * ratio);
			width = pMaxWidth;
		}
		BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedResizedImage.createGraphics();
		Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		//hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		//hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHints(hints);
		//		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
		g2d.dispose();
		ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(encoderOutputStream);
		encoder.encode(bufferedResizedImage);
		return encoderOutputStream.toByteArray();
	}
	/*
	public static ImageData cropImage(Image image, double sx, double sy, double sw, double sh) {

		Canvas canvasTmp = Canvas.createIfSupported();
		Context2d context = canvasTmp.getContext2d();

		canvasTmp.setCoordinateSpaceHeight((int) sh+10);
		canvasTmp.setCoordinateSpaceWidth((int) sw+10);

		ImageElement imageElement = ImageElement.as(image.getElement());

		double dx = 0;
		double dy = 0;
		double dw = sw;
		double dh = sh;

		// draw image to canvas
		context.drawImage(imageElement, sx, sy, sw, sh, dx, dy, dw, dh);

		// get image data
		double w = sw;
		double h = sh;
		ImageData imageData = context.getImageData(0, 0, w, h);

		canvasTmp.removeFromParent();

		return imageData;
	}

	public static ImageData scaleAndCropImage(Image image, double scaleToRatio, double sx, double sy, double sw, double sh) {

		Canvas canvasTmp = Canvas.createIfSupported();
		//RootPanel.get().add(canvasTmp);
		Context2d context = canvasTmp.getContext2d();

		double ch = (image.getHeight() * scaleToRatio) + 100;
		double cw = (image.getWidth() * scaleToRatio) + 100;

		canvasTmp.setCoordinateSpaceHeight((int) ch);
		canvasTmp.setCoordinateSpaceWidth((int) cw);

		ImageElement imageElement = ImageElement.as(image.getElement());

		// tell it to scale image
		context.scale(scaleToRatio, scaleToRatio);

		// draw image to canvas
		// s = source
		// d = destination     
		double dx = 0;
		double dy = 0;
		context.drawImage(imageElement, dx, dy);

		// get image data - if you go greater than the scaled image nothing will show up
		ImageData imageData = context.getImageData(sx, sy, sw, sh);

		return imageData;
	}

	
	public static void addImageToScreen(ImageData imageData) {

		Canvas canvasTmp = Canvas.createIfSupported();
		canvasTmp.setCoordinateSpaceHeight((int) imageData.getHeight()+10);
		canvasTmp.setCoordinateSpaceWidth((int) imageData.getWidth()+10);
		Context2d context = canvasTmp.getContext2d();

		context.putImageData(imageData, 0, 0);

		//flexTable.setWidget(r, c, canvasTmp);
		RootPanel.get().add(canvasTmp);
	}


	public static ImageData scaleImage(Image image, double scaleToRatio) {

		//System.out.println("PanoTiler.scaleImag()e: scaleToRatio=" + scaleToRatio + " width=" + width + " x height=" + height);

		Canvas canvasTmp = Canvas.createIfSupported();
		Context2d context = canvasTmp.getContext2d();

		double ch = (image.getHeight() * scaleToRatio) + 100; // 100 is offset so it doesn't throw
		double cw = (image.getWidth() * scaleToRatio) + 100;

		canvasTmp.setCoordinateSpaceHeight((int) ch);
		canvasTmp.setCoordinateSpaceWidth((int) cw);

		ImageElement imageElement = ImageElement.as(image.getElement());

		// s = source
		// d = destination 
		double sx = 0;
		double sy = 0;
		double sw = imageElement.getWidth();
		double sh = imageElement.getHeight();

		double dx = 0;
		double dy = 0;
		double dw = imageElement.getWidth();
		double dh = imageElement.getHeight();

		// tell it to scale image
		context.scale(scaleToRatio, scaleToRatio);

		// draw image to canvas
		context.drawImage(imageElement, sx, sy, sw, sh, dx, dy, dw, dh);

		// get image data
		double w = dw * scaleToRatio;
		double h = dh * scaleToRatio;
		ImageData imageData = context.getImageData(0, 0, w, h); // this won't get the extra 100

		return imageData;
	}

	public static ImageData cropImage(ImageData imageData, double sx, double sy, double sw, double sh) {

		Canvas canvasTmp = Canvas.createIfSupported();
		canvasTmp.setStyleName("mainCanvas");
		Context2d context = canvasTmp.getContext2d();

		canvasTmp.setCoordinateSpaceHeight((int) imageData.getHeight()+10);
		canvasTmp.setCoordinateSpaceWidth((int) imageData.getWidth()+10);

		// draw image to canvas
		context.putImageData(imageData, 0, 0);

		// get image data
		//imageData = context.getImageData(0, 0, imageData.getWidth(), imageData.getHeight());
		ImageData newImageData = context.getImageData(sx, sy, sw, sh);

		return newImageData;
	}
	*/
}