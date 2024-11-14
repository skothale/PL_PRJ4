public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
        init_lexer();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
        init_lexer();
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'init_lexer'");
        this.lex = new LexerImpl();

        // Add automata for each token type using helper method
        lex.add_automaton(TokenType.NUM, createAutomatonForNum());
        lex.add_automaton(TokenType.PLUS, createAutomatonForSymbol('+'));
        lex.add_automaton(TokenType.MINUS, createAutomatonForSymbol('-'));
        lex.add_automaton(TokenType.TIMES, createAutomatonForSymbol('*'));
        lex.add_automaton(TokenType.DIV, createAutomatonForSymbol('/'));
        lex.add_automaton(TokenType.LPAREN, createAutomatonForSymbol('('));
        lex.add_automaton(TokenType.RPAREN, createAutomatonForSymbol(')'));
        lex.add_automaton(TokenType.WHITE_SPACE, createAutomatonForWhiteSpace());

    }
    private Automaton createAutomatonForNum() {
        AutomatonImpl automaton = new AutomatonImpl();
        int startState = 0;
        int preDecimalState = 1;
        int decimalPointState = 2;
        int postDecimalState = 3;
        automaton.addState(startState, true, false);
        automaton.addState(preDecimalState, false, false);
        automaton.addState(decimalPointState, false, false);
        automaton.addState(postDecimalState, false, true);
        addDigitTransitions(automaton, startState, preDecimalState);
        addDigitTransitions(automaton, decimalPointState, postDecimalState);
        automaton.addTransition(preDecimalState, '.', decimalPointState);
        automaton.addTransition(startState, '.', decimalPointState);

        return automaton;
    }

    private void addDigitTransitions(AutomatonImpl automaton, int fromState, int toState) {
        for (char c = '0'; c <= '9'; c++) {
            automaton.addTransition(fromState, c, toState);
            automaton.addTransition(toState, c, toState);
        }
    }

    private Automaton createAutomatonForSymbol(char symbol) {
        AutomatonImpl automaton = new AutomatonImpl();

        int startState = 0;
        int acceptState = 1;
        automaton.addState(startState, true, false);
        automaton.addState(acceptState, false, true);
        automaton.addTransition(startState, symbol, acceptState);
        return automaton;
    }

    private Automaton createAutomatonForWhiteSpace() {
        AutomatonImpl automaton = new AutomatonImpl();

        int startState = 0;
        automaton.addState(startState, true, true); 
        automaton.addTransition(startState, ' ', startState);
        automaton.addTransition(startState, '\n', startState);
        automaton.addTransition(startState, '\r', startState);
        automaton.addTransition(startState, '\t', startState);

        return automaton;
    }
}
