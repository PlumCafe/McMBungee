/*   1:    */ package org.json.simple;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Reader;
/*   5:    */ import java.io.StringReader;
/*   6:    */ import java.io.Writer;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.json.simple.parser.JSONParser;
/*  10:    */ import org.json.simple.parser.ParseException;
/*  11:    */ 
/*  12:    */ public class JSONValue
/*  13:    */ {
/*  14:    */   public static Object parse(Reader in)
/*  15:    */   {
/*  16:    */     try
/*  17:    */     {
/*  18: 41 */       JSONParser parser = new JSONParser();
/*  19: 42 */       return parser.parse(in);
/*  20:    */     }
/*  21:    */     catch (Exception e) {}
/*  22: 45 */     return null;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static Object parse(String s)
/*  26:    */   {
/*  27: 50 */     StringReader in = new StringReader(s);
/*  28: 51 */     return parse(in);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Object parseWithException(Reader in)
/*  32:    */     throws IOException, ParseException
/*  33:    */   {
/*  34: 72 */     JSONParser parser = new JSONParser();
/*  35: 73 */     return parser.parse(in);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static Object parseWithException(String s)
/*  39:    */     throws ParseException
/*  40:    */   {
/*  41: 77 */     JSONParser parser = new JSONParser();
/*  42: 78 */     return parser.parse(s);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void writeJSONString(Object value, Writer out)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 96 */     if (value == null)
/*  49:    */     {
/*  50: 97 */       out.write("null");
/*  51: 98 */       return;
/*  52:    */     }
/*  53:101 */     if ((value instanceof String))
/*  54:    */     {
/*  55:102 */       out.write(34);
/*  56:103 */       out.write(escape((String)value));
/*  57:104 */       out.write(34);
/*  58:105 */       return;
/*  59:    */     }
/*  60:108 */     if ((value instanceof Double))
/*  61:    */     {
/*  62:109 */       if ((((Double)value).isInfinite()) || (((Double)value).isNaN())) {
/*  63:110 */         out.write("null");
/*  64:    */       } else {
/*  65:112 */         out.write(value.toString());
/*  66:    */       }
/*  67:113 */       return;
/*  68:    */     }
/*  69:116 */     if ((value instanceof Float))
/*  70:    */     {
/*  71:117 */       if ((((Float)value).isInfinite()) || (((Float)value).isNaN())) {
/*  72:118 */         out.write("null");
/*  73:    */       } else {
/*  74:120 */         out.write(value.toString());
/*  75:    */       }
/*  76:121 */       return;
/*  77:    */     }
/*  78:124 */     if ((value instanceof Number))
/*  79:    */     {
/*  80:125 */       out.write(value.toString());
/*  81:126 */       return;
/*  82:    */     }
/*  83:129 */     if ((value instanceof Boolean))
/*  84:    */     {
/*  85:130 */       out.write(value.toString());
/*  86:131 */       return;
/*  87:    */     }
/*  88:134 */     if ((value instanceof JSONStreamAware))
/*  89:    */     {
/*  90:135 */       ((JSONStreamAware)value).writeJSONString(out);
/*  91:136 */       return;
/*  92:    */     }
/*  93:139 */     if ((value instanceof JSONAware))
/*  94:    */     {
/*  95:140 */       out.write(((JSONAware)value).toJSONString());
/*  96:141 */       return;
/*  97:    */     }
/*  98:144 */     if ((value instanceof Map))
/*  99:    */     {
/* 100:145 */       JSONObject.writeJSONString((Map)value, out);
/* 101:146 */       return;
/* 102:    */     }
/* 103:149 */     if ((value instanceof List))
/* 104:    */     {
/* 105:150 */       JSONArray.writeJSONString((List)value, out);
/* 106:151 */       return;
/* 107:    */     }
/* 108:154 */     out.write(value.toString());
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static String toJSONString(Object value)
/* 112:    */   {
/* 113:172 */     if (value == null) {
/* 114:173 */       return "null";
/* 115:    */     }
/* 116:175 */     if ((value instanceof String)) {
/* 117:176 */       return "\"" + escape((String)value) + "\"";
/* 118:    */     }
/* 119:178 */     if ((value instanceof Double))
/* 120:    */     {
/* 121:179 */       if ((((Double)value).isInfinite()) || (((Double)value).isNaN())) {
/* 122:180 */         return "null";
/* 123:    */       }
/* 124:182 */       return value.toString();
/* 125:    */     }
/* 126:185 */     if ((value instanceof Float))
/* 127:    */     {
/* 128:186 */       if ((((Float)value).isInfinite()) || (((Float)value).isNaN())) {
/* 129:187 */         return "null";
/* 130:    */       }
/* 131:189 */       return value.toString();
/* 132:    */     }
/* 133:192 */     if ((value instanceof Number)) {
/* 134:193 */       return value.toString();
/* 135:    */     }
/* 136:195 */     if ((value instanceof Boolean)) {
/* 137:196 */       return value.toString();
/* 138:    */     }
/* 139:198 */     if ((value instanceof JSONAware)) {
/* 140:199 */       return ((JSONAware)value).toJSONString();
/* 141:    */     }
/* 142:201 */     if ((value instanceof Map)) {
/* 143:202 */       return JSONObject.toJSONString((Map)value);
/* 144:    */     }
/* 145:204 */     if ((value instanceof List)) {
/* 146:205 */       return JSONArray.toJSONString((List)value);
/* 147:    */     }
/* 148:207 */     return value.toString();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static String escape(String s)
/* 152:    */   {
/* 153:216 */     if (s == null) {
/* 154:217 */       return null;
/* 155:    */     }
/* 156:218 */     StringBuffer sb = new StringBuffer();
/* 157:219 */     escape(s, sb);
/* 158:220 */     return sb.toString();
/* 159:    */   }
/* 160:    */   
/* 161:    */   static void escape(String s, StringBuffer sb)
/* 162:    */   {
/* 163:228 */     for (int i = 0; i < s.length(); i++)
/* 164:    */     {
/* 165:229 */       char ch = s.charAt(i);
/* 166:230 */       switch (ch)
/* 167:    */       {
/* 168:    */       case '"': 
/* 169:232 */         sb.append("\\\"");
/* 170:233 */         break;
/* 171:    */       case '\\': 
/* 172:235 */         sb.append("\\\\");
/* 173:236 */         break;
/* 174:    */       case '\b': 
/* 175:238 */         sb.append("\\b");
/* 176:239 */         break;
/* 177:    */       case '\f': 
/* 178:241 */         sb.append("\\f");
/* 179:242 */         break;
/* 180:    */       case '\n': 
/* 181:244 */         sb.append("\\n");
/* 182:245 */         break;
/* 183:    */       case '\r': 
/* 184:247 */         sb.append("\\r");
/* 185:248 */         break;
/* 186:    */       case '\t': 
/* 187:250 */         sb.append("\\t");
/* 188:251 */         break;
/* 189:    */       case '/': 
/* 190:253 */         sb.append("\\/");
/* 191:254 */         break;
/* 192:    */       default: 
/* 193:257 */         if (((ch >= 0) && (ch <= '\037')) || ((ch >= '') && (ch <= '')) || ((ch >= ' ') && (ch <= '⃿')))
/* 194:    */         {
/* 195:258 */           String ss = Integer.toHexString(ch);
/* 196:259 */           sb.append("\\u");
/* 197:260 */           for (int k = 0; k < 4 - ss.length(); k++) {
/* 198:261 */             sb.append('0');
/* 199:    */           }
/* 200:263 */           sb.append(ss.toUpperCase());
/* 201:    */         }
/* 202:    */         else
/* 203:    */         {
/* 204:266 */           sb.append(ch);
/* 205:    */         }
/* 206:    */         break;
/* 207:    */       }
/* 208:    */     }
/* 209:    */   }
/* 210:    */ }


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.JSONValue
 * JD-Core Version:    0.7.0.1
 */