import br.com.etyllica.Etyllica;
import br.com.etyllica.context.Application;
import br.com.prodec.autofinder.SignFinder;


public class VideoFinder extends Etyllica {

	public VideoFinder() {
		super(720, 480);
	}

	@Override
	public Application startApplication() {
		
		return new SignFinder(w, h);
		
	}

}
