package colladaLoader;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
public class ColladaLoaderException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ColladaLoaderException(String msg, RuntimeException ex) {
        super(msg+"\n throwed by a "+ex.getClass().getName()+"\n");
        setStackTrace(ex.getStackTrace());
    }

}
