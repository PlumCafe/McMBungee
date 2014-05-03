/*   1:    */ package org.json.simple;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ 
/*   9:    */ public class JSONArray
/*  10:    */   extends ArrayList
/*  11:    */   implements List, JSONAware, JSONStreamAware
/*  12:    */ {
/*  13:    */   private static final long serialVersionUID = 3957988303675231981L;
/*  14:    */   
/*  15:    */   public static void writeJSONString(List list, Writer out)
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 32 */     if (list == null)
/*  19:    */     {
/*  20: 33 */       out.write("null");
/*  21: 34 */       return;
/*  22:    */     }
/*  23: 37 */     boolean first = true;
/*  24: 38 */     Iterator iter = list.iterator();
/*  25:    */     
/*  26: 40 */     out.write(91);
/*  27: 41 */     while (iter.hasNext())
/*  28:    */     {
/*  29: 42 */       if (first) {
/*  30: 43 */         first = false;
/*  31:    */       } else {
/*  32: 45 */         out.write(44);
/*  33:    */       }
/*  34: 47 */       Object value = iter.next();
/*  35: 48 */       if (value == null) {
/*  36: 49 */         out.write("null");
/*  37:    */       } else {
/*  38: 53 */         JSONValue.writeJSONString(value, out);
/*  39:    */       }
/*  40:    */     }
/*  41: 55 */     out.write(93);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void writeJSONString(Writer out)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47: 59 */     writeJSONString(this, out);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static String toJSONString(List list)
/*  51:    */   {
/*  52: 72 */     if (list == null) {
/*  53: 73 */       return "null";
/*  54:    */     }
/*  55: 75 */     boolean first = true;
/*  56: 76 */     StringBuffer sb = new StringBuffer();
/*  57: 77 */     Iterator iter = list.iterator();
/*  58:    */     
/*  59: 79 */     sb.append('[');
/*  60: 80 */     while (iter.hasNext())
/*  61:    */     {
/*  62: 81 */       if (first) {
/*  63: 82 */         first = false;
/*  64:    */       } else {
/*  65: 84 */         sb.append(',');
/*  66:    */       }
/*  67: 86 */       Object value = iter.next();
/*  68: 87 */       if (value == null) {
/*  69: 88 */         sb.append("null");
/*  70:    */       } else {
/*  71: 91 */         sb.append(JSONValue.toJSONString(value));
/*  72:    */       }
/*  73:    */     }
/*  74: 93 */     sb.append(']');
/*  75: 94 */     return sb.toString();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toJSONString()
/*  79:    */   {
/*  80: 98 */     return toJSONString(this);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:102 */     return toJSONString();
/*  86:    */   }
/*  87:    */ }


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.JSONArray
 * JD-Core Version:    0.7.0.1
 */