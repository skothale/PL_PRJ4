
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'do_parse'");
        return parseT(); 
    }
    private Expr parseT() throws Exception {
        Expr expr = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(expr, right);
            } else {
                return new MinusExpr(expr, right);
            }
        }
        return expr;
    }

    private Expr parseF() throws Exception {
        Expr expr = parseLit(); 

        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr right = parseF(); 
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(expr, right);
            } else {
                return new DivExpr(expr, right);
            }
        }
        return expr;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) { 
            Token numToken = consume(TokenType.NUM); 
            return new FloatExpr(Float.parseFloat(numToken.lexeme)); 
        }
        if (peek(TokenType.LPAREN, 0)) { 
            consume(TokenType.LPAREN);
            Expr expr = parseT();
            consume(TokenType.RPAREN);
            return expr; 
        }
        throw new Exception("Parsing error: expected NUM or LPAREN");
    }

}
