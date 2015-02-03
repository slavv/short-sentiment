package core.pipe;

import gate.util.GateException;

import java.io.IOException;
import java.io.Serializable;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;
import core.GateProcessor;

public class CharSequence2StemmedTokenSequence extends Pipe implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	GateProcessor processor;

	public CharSequence2StemmedTokenSequence() {
		processor = new GateProcessor();
		try {
			processor.initialize();
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Instance pipe(Instance carrier) {
		CharSequence charSequence = (CharSequence) carrier.getData();
		String dataString = charSequence.toString();
		String[] tokens = {};
		try {
			tokens = processor.processString(dataString);
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TokenSequence ts = new TokenSequence(tokens);
		carrier.setData(ts);
		return carrier;
	}
}
