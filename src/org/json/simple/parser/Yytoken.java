/*  1:   */ package org.json.simple.parser;
/*  2:   */ 
/*  3:   */ public class Yytoken
/*  4:   */ {
/*  5:   */   public static final int TYPE_VALUE = 0;
/*  6:   */   public static final int TYPE_LEFT_BRACE = 1;
/*  7:   */   public static final int TYPE_RIGHT_BRACE = 2;
/*  8:   */   public static final int TYPE_LEFT_SQUARE = 3;
/*  9:   */   public static final int TYPE_RIGHT_SQUARE = 4;
/* 10:   */   public static final int TYPE_COMMA = 5;
/* 11:   */   public static final int TYPE_COLON = 6;
/* 12:   */   public static final int TYPE_EOF = -1;
/* 13:20 */   public int type = 0;
/* 14:21 */   public Object value = null;
/* 15:   */   
/* 16:   */   public Yytoken(int type, Object value)
/* 17:   */   {
/* 18:24 */     this.type = type;
/* 19:25 */     this.value = value;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toString()
/* 23:   */   {
/* 24:29 */     StringBuffer sb = new StringBuffer();
/* 25:30 */     switch (this.type)
/* 26:   */     {
/* 27:   */     case 0: 
/* 28:32 */       sb.append("VALUE(").append(this.value).append(")");
/* 29:33 */       break;
/* 30:   */     case 1: 
/* 31:35 */       sb.append("LEFT BRACE({)");
/* 32:36 */       break;
/* 33:   */     case 2: 
/* 34:38 */       sb.append("RIGHT BRACE(})");
/* 35:39 */       break;
/* 36:   */     case 3: 
/* 37:41 */       sb.append("LEFT SQUARE([)");
/* 38:42 */       break;
/* 39:   */     case 4: 
/* 40:44 */       sb.append("RIGHT SQUARE(])");
/* 41:45 */       break;
/* 42:   */     case 5: 
/* 43:47 */       sb.append("COMMA(,)");
/* 44:48 */       break;
/* 45:   */     case 6: 
/* 46:50 */       sb.append("COLON(:)");
/* 47:51 */       break;
/* 48:   */     case -1: 
/* 49:53 */       sb.append("END OF FILE");
/* 50:   */     }
/* 51:56 */     return sb.toString();
/* 52:   */   }
/* 53:   */ }


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.parser.Yytoken
 * JD-Core Version:    0.7.0.1
 */