// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.parsing.TokenReader;

import java.lang.Character;
import java.lang.Double;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private final String str;
  private int ind;

  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    str = source;
    ind = 0;
  }

  private void moveSpaces() {
    while (ind < str.length() && Character.isWhitespace(str.charAt(ind))) {
      ind++;
    }
  }

  private boolean isSymbol(char c) {
    return c == '=' || c == '+' || c == '-' || c == ';';
  }

  private boolean out() {
    return ind >= str.length();
  }

  private boolean isNameCharacter(char c) {
    return !Character.isWhitespace(c) && !Character.isDigit(c) && c != '\"' && !isSymbol(c);
  }

  // start with letter
  private NameToken getName() {
    int start = ind;
    while (!out() && isNameCharacter(str.charAt(ind))) {
      ind++;
    }
    String sub = str.substring(start, ind);
    return sub.length() == 0 ? null : new NameToken(sub);
  }

  private NumberToken getNumber() throws IOException {
    int start = ind;
    while (!out() && Character.isDigit(str.charAt(ind))) {
      ind++;
    }
    if (!out() && str.charAt(ind) == '.') {
      ind++;
    }
    while (!out() && Character.isDigit(str.charAt(ind))) {
      ind++;
    }

    String sub = str.substring(start, ind);
    return sub.length() == 0? null : new NumberToken(Double.parseDouble(sub));
  }

  private StringToken getString() throws IOException {
    ind++;
    int start = ind;
    while (!out() && str.charAt(ind) != '\"') {
      if (str.charAt(ind) == '\n') {
        throw new IOException();
      }
      ind++;
    }
    if (out()) {
      return null;
    }
    String sub = str.substring(start, ind);
    ind++;
    return new StringToken(sub);
  }

  private SymbolToken getSymbol() {
    char symbol = str.charAt(ind);
    ind++;
    return new SymbolToken(symbol);
  }

  private Token getToken() throws IOException {
    char start = str.charAt(ind);
    Token ans = null;
    if (Character.isLetter(start)) {
      ans = getName();
    } else if (Character.isDigit(start)) {
      ans = getNumber();
    } else if (start == '\"') {
      ans = getString();
    } else if (isSymbol(start)) {
      ans = getSymbol();
    } else {
      throw new IOException();
    }
    return ans;
  }

  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.

    moveSpaces();
    if (out())
      return null;
    return getToken();
  }
}
