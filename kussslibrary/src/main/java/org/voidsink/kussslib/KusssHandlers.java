package org.voidsink.kussslib;

import org.voidsink.kussslib.impl.KusssHandlerImpl;

public final class KusssHandlers {

	private static volatile KusssHandler instance = null;
	private static final Object mutex = new Object();
	
    public static KusssHandler getInstance() {
    	if (instance == null) {
			synchronized (mutex) {
				if (instance==null) instance= new KusssHandlerImpl();
			}
		}
		return instance;    
	}
    
    
	private KusssHandlers() {
		throw new UnsupportedOperationException();
	}	
}
