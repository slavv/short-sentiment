package core.pipe;

import java.io.Serializable;

import cc.mallet.extract.StringSpan;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;

public class TokenSequence2NgramTokenSequence extends Pipe implements
		Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int n = 1;

	public TokenSequence2NgramTokenSequence(int n) {
		this.n = n;
	}

	@Override
	public Instance pipe(Instance carrier) {
		TokenSequence tokenSequence = (TokenSequence) carrier.getData();
		StringSpan[] tokens = tokenSequence.toArray(new StringSpan[0]);
		for (int h = 2; h <= n; h++) {
			int ngramsCount = tokens.length - h + 1;
			if(ngramsCount <= 0) break;
			String[] ngramTokens = new String[ngramsCount];
			for (int i = 0; i < ngramsCount; i++) {
				String ngram = "";
				for (int j = 0; j < h; j++) {
					ngram += tokens[i + j].getText() + " ";
				}
				ngramTokens[i] = ngram.trim();
			}
			tokenSequence.addAll(ngramTokens);
		}
		carrier.setData(tokenSequence);
		return carrier;
	}
}
