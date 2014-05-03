/*  1:   */ package org.json.simple.parser;
/*  2:   */ 
/*  3:   */ public class ParseException
/*  4:   */   extends Exception
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = -7880698968187728548L;
/*  7:   */   public static final int ERROR_UNEXPECTED_CHAR = 0;
/*  8:   */   public static final int ERROR_UNEXPECTED_TOKEN = 1;
/*  9:   */   public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
/* 10:   */   private int errorType;
/* 11:   */   private Object unexpectedObject;
/* 12:   */   private int position;
/* 13:   */   
/* 14:   */   public ParseException(int errorType)
/* 15:   */   {
/* 16:21 */     this(-1, errorType, null);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public ParseException(int errorType, Object unexpectedObject)
/* 20:   */   {
/* 21:25 */     this(-1, errorType, unexpectedObject);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ParseException(int position, int errorType, Object unexpectedObject)
/* 25:   */   {
/* 26:29 */     this.position = position;
/* 27:30 */     this.errorType = errorType;
/* 28:31 */     this.unexpectedObject = unexpectedObject;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int getErrorType()
/* 32:   */   {
/* 33:35 */     return this.errorType;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void setErrorType(int errorType)
/* 37:   */   {
/* 38:39 */     this.errorType = errorType;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getPosition()
/* 42:   */   {
/* 43:48 */     return this.position;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setPosition(int position)
/* 47:   */   {
/* 48:52 */     this.position = position;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public Object getUnexpectedObject()
/* 52:   */   {
/* 53:64 */     return this.unexpectedObject;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void setUnexpectedObject(Object unexpectedObject)
/* 57:   */   {
/* 58:68 */     this.unexpectedObject = unexpectedObject;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public String toString()
/* 62:   */   {
/* 63:72 */     StringBuffer sb = new StringBuffer();
/* 64:74 */     switch (this.errorType)
/* 65:   */     {
/* 66:   */     case 0: 
/* 67:76 */       sb.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
/* 68:77 */       break;
/* 69:   */     case 1: 
/* 70:79 */       sb.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
/* 71:80 */       break;
/* 72:   */     case 2: 
/* 73:82 */       sb.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
/* 74:83 */       break;
/* 75:   */     default: 
/* 76:85 */       sb.append("Unkown error at position ").append(this.position).append(".");
/* 77:   */     }
/* 78:88 */     return sb.toString();
/* 79:   */   }
/* 80:   */ }


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.parser.ParseException
 * JD-Core Version:    0.7.0.1
 */