package core.pipe;

import java.io.Serializable;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;

public class TokenSequencePOSTokenSequence extends Pipe implements
		Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int n = 1;

	public TokenSequencePOSTokenSequence(int n) {
		this.n = n;
	}

	@Override
	public Instance pipe(Instance carrier) {
		TokenSequence tokenSequence = (TokenSequence) carrier.getData();
		for (int h = 2; h <= n; h++) {
			int ngramsCount = tokenSequence.size() - h + 1;
			if(ngramsCount <= 0) break;
			String[] ngramTokens = new String[ngramsCount];
			for (int i = 0; i < ngramsCount; i++) {
				String ngram = "";
				for (int j = 0; j < h; j++) {
					ngram += tokenSequence.get(i + j).getText() + " ";
				}
				ngramTokens[i] = ngram.trim();
			}
			tokenSequence.addAll(ngramTokens);
		}
		carrier.setData(tokenSequence);
		return carrier;
	}
}
