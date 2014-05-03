/*   1:    */ package org.json.simple;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ 
/*  11:    */ public class JSONObject
/*  12:    */   extends HashMap
/*  13:    */   implements Map, JSONAware, JSONStreamAware
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = -503443796854799292L;
/*  16:    */   
/*  17:    */   public JSONObject() {}
/*  18:    */   
/*  19:    */   public JSONObject(Map map)
/*  20:    */   {
/*  21: 34 */     super(map);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static void writeJSONString(Map map, Writer out)
/*  25:    */     throws IOException
/*  26:    */   {
/*  27: 48 */     if (map == null)
/*  28:    */     {
/*  29: 49 */       out.write("null");
/*  30: 50 */       return;
/*  31:    */     }
/*  32: 53 */     boolean first = true;
/*  33: 54 */     Iterator iter = map.entrySet().iterator();
/*  34:    */     
/*  35: 56 */     out.write(123);
/*  36: 57 */     while (iter.hasNext())
/*  37:    */     {
/*  38: 58 */       if (first) {
/*  39: 59 */         first = false;
/*  40:    */       } else {
/*  41: 61 */         out.write(44);
/*  42:    */       }
/*  43: 62 */       Map.Entry entry = (Map.Entry)iter.next();
/*  44: 63 */       out.write(34);
/*  45: 64 */       out.write(escape(String.valueOf(entry.getKey())));
/*  46: 65 */       out.write(34);
/*  47: 66 */       out.write(58);
/*  48: 67 */       JSONValue.writeJSONString(entry.getValue(), out);
/*  49:    */     }
/*  50: 69 */     out.write(125);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void writeJSONString(Writer out)
/*  54:    */     throws IOException
/*  55:    */   {
/*  56: 73 */     writeJSONString(this, out);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static String toJSONString(Map map)
/*  60:    */   {
/*  61: 86 */     if (map == null) {
/*  62: 87 */       return "null";
/*  63:    */     }
/*  64: 89 */     StringBuffer sb = new StringBuffer();
/*  65: 90 */     boolean first = true;
/*  66: 91 */     Iterator iter = map.entrySet().iterator();
/*  67:    */     
/*  68: 93 */     sb.append('{');
/*  69: 94 */     while (iter.hasNext())
/*  70:    */     {
/*  71: 95 */       if (first) {
/*  72: 96 */         first = false;
/*  73:    */       } else {
/*  74: 98 */         sb.append(',');
/*  75:    */       }
/*  76:100 */       Map.Entry entry = (Map.Entry)iter.next();
/*  77:101 */       toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
/*  78:    */     }
/*  79:103 */     sb.append('}');
/*  80:104 */     return sb.toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toJSONString()
/*  84:    */   {
/*  85:108 */     return toJSONString(this);
/*  86:    */   }
/*  87:    */   
/*  88:    */   private static String toJSONString(String key, Object value, StringBuffer sb)
/*  89:    */   {
/*  90:112 */     sb.append('"');
/*  91:113 */     if (key == null) {
/*  92:114 */       sb.append("null");
/*  93:    */     } else {
/*  94:116 */       JSONValue.escape(key, sb);
/*  95:    */     }
/*  96:117 */     sb.append('"').append(':');
/*  97:    */     
/*  98:119 */     sb.append(JSONValue.toJSONString(value));
/*  99:    */     
/* 100:121 */     return sb.toString();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String toString()
/* 104:    */   {
/* 105:125 */     return toJSONString();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static String toString(String key, Object value)
/* 109:    */   {
/* 110:129 */     StringBuffer sb = new StringBuffer();
/* 111:130 */     toJSONString(key, value, sb);
/* 112:131 */     return sb.toString();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static String escape(String s)
/* 116:    */   {
/* 117:144 */     return JSONValue.escape(s);
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.JSONObject
 * JD-Core Version:    0.7.0.1
 */