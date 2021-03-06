package com.wearezeta.auto.ios.pages.keyboard;

import org.openqa.selenium.WebElement;

class KeyboardStateAlpha extends KeyboardState {

	public KeyboardStateAlpha(WebElement keyboard) {
		super(keyboard);
	}

	@Override
	public void switchTo(KeyboardState finalState) throws InterruptedException {
		if (finalState instanceof KeyboardStateAlpha) {
			return;
		} else if (finalState instanceof KeyboardStateAlphaCaps) {
			tapSpecialKey(SHIFT);
		} else if (finalState instanceof KeyboardStateNumbers) {
			tapSpecialKey(MORE_NUMBERS);
		} else if (finalState instanceof KeyboardStateSpecial) {
			tapSpecialKey(MORE_NUMBERS);
			tapSpecialKey(SHIFT);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getCharacterSetPattern() {
		return "[a-z\\s]";
	}

	@Override
	public String getFirstCharacter() {
		return "a";
	}

}
