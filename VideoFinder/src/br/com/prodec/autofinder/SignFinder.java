package br.com.prodec.autofinder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.input.mouse.MouseButton;
import br.com.etyllica.layer.BufferedLayer;
import br.com.etyllica.motion.core.features.Component;
import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.motion.filter.search.TriangularSearch;
import br.com.etyllica.motion.modifier.hull.FastConvexHullModifier;


public class SignFinder extends Application {

	private BufferedLayer layer = null;
	
	private TriangularSearch filter;
	
	private ColorStrategy colorStrategy;
	
	public SignFinder(int w, int h) {
		super(w, h);
	}
	
	private int frameNumber = 38000;
	
	private Component screen = new Component(0, 50, w, h-50);

	private List<Component> components = new ArrayList<Component>(); 
	
	@Override
	public void load() {
					    
		reloadFrame();
				
		filter = new TriangularSearch(w, h);
		
		filter.setComponentModifierStrategy(new FastConvexHullModifier());
		
		colorStrategy = new ColorStrategy(Color.BLACK);
		colorStrategy.setTolerance(0x10);
				
		filter.setPixelStrategy(colorStrategy);
		
		this.filter();
		
		loading = 100;
	}
	
	private void reloadFrame() {
		
		try {

			final String BIN_PATH = getClass().getResource("../../../../").toString();
			
			BufferedImage frame = FrameGrab.getFrame(new File(BIN_PATH+"video/working_road.mp4"), frameNumber);
			layer = new BufferedLayer(frame);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void update(long now) {

		//Now we search for the first pixel with the desired color in the whole screen
		//components = filter.filter(layer.getModifiedBuffer(), screen);
		
		//System.out.println("UPDATE!");

	}
	
	private void filter() {
		components = filter.filter(layer.getModifiedBuffer(), screen);
	}
	
	@Override
	public void draw(Graphic g) {
		
		layer.draw(g);

		g.setFont(g.getFont().deriveFont(40f));
		g.drawShadow(20, 80, Integer.toString(frameNumber));
		
		g.setColor(Color.BLACK);
		
		for(Component component: components) {

			g.setColor(Color.RED);
			
			g.setStroke(new BasicStroke(3f));
			
			g.drawRect(component.getRectangle());
						
		}
		
	}
	
	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {

			colorStrategy.setColor(layer.getModifiedBuffer().getRGB((int)event.getX(), (int)event.getY()));
			
			this.filter();
						
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {
		
		if(event.isKeyDown(KeyEvent.TSK_SETA_DIREITA)) {
			frameNumber += 1000;
			reloadFrame();
		}
		
		if(event.isKeyDown(KeyEvent.TSK_SETA_ESQUERDA)) {
			frameNumber -= 1000;
			reloadFrame();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
