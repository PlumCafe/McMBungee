/*   1:    */ package org.json.simple.parser;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Reader;
/*   5:    */ import java.io.StringReader;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.json.simple.JSONArray;
/*  10:    */ import org.json.simple.JSONObject;
/*  11:    */ 
/*  12:    */ public class JSONParser
/*  13:    */ {
/*  14:    */   public static final int S_INIT = 0;
/*  15:    */   public static final int S_IN_FINISHED_VALUE = 1;
/*  16:    */   public static final int S_IN_OBJECT = 2;
/*  17:    */   public static final int S_IN_ARRAY = 3;
/*  18:    */   public static final int S_PASSED_PAIR_KEY = 4;
/*  19:    */   public static final int S_IN_PAIR_VALUE = 5;
/*  20:    */   public static final int S_END = 6;
/*  21:    */   public static final int S_IN_ERROR = -1;
/*  22:    */   private LinkedList handlerStatusStack;
/*  23: 34 */   private Yylex lexer = new Yylex((Reader)null);
/*  24: 35 */   private Yytoken token = null;
/*  25: 36 */   private int status = 0;
/*  26:    */   
/*  27:    */   private int peekStatus(LinkedList statusStack)
/*  28:    */   {
/*  29: 39 */     if (statusStack.size() == 0) {
/*  30: 40 */       return -1;
/*  31:    */     }
/*  32: 41 */     Integer status = (Integer)statusStack.getFirst();
/*  33: 42 */     return status.intValue();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void reset()
/*  37:    */   {
/*  38: 50 */     this.token = null;
/*  39: 51 */     this.status = 0;
/*  40: 52 */     this.handlerStatusStack = null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void reset(Reader in)
/*  44:    */   {
/*  45: 63 */     this.lexer.yyreset(in);
/*  46: 64 */     reset();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getPosition()
/*  50:    */   {
/*  51: 71 */     return this.lexer.yystate();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Object parse(String s)
/*  55:    */     throws ParseException
/*  56:    */   {
/*  57: 75 */     return parse(s, (ContainerFactory)null);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Object parse(String s, ContainerFactory containerFactory)
/*  61:    */     throws ParseException
/*  62:    */   {
/*  63: 79 */     StringReader in = new StringReader(s);
/*  64:    */     try
/*  65:    */     {
/*  66: 81 */       return parse(in, containerFactory);
/*  67:    */     }
/*  68:    */     catch (IOException ie)
/*  69:    */     {
/*  70: 87 */       throw new ParseException(-1, 2, ie);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Object parse(Reader in)
/*  75:    */     throws IOException, ParseException
/*  76:    */   {
/*  77: 92 */     return parse(in, (ContainerFactory)null);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Object parse(Reader in, ContainerFactory containerFactory)
/*  81:    */     throws IOException, ParseException
/*  82:    */   {
/*  83:112 */     reset(in);
/*  84:113 */     LinkedList statusStack = new LinkedList();
/*  85:114 */     LinkedList valueStack = new LinkedList();
/*  86:    */     try
/*  87:    */     {
/*  88:    */       do
/*  89:    */       {
/*  90:118 */         nextToken();
/*  91:119 */         switch (this.status)
/*  92:    */         {
/*  93:    */         case 0: 
/*  94:121 */           switch (this.token.type)
/*  95:    */           {
/*  96:    */           case 0: 
/*  97:123 */             this.status = 1;
/*  98:124 */             statusStack.addFirst(new Integer(this.status));
/*  99:125 */             valueStack.addFirst(this.token.value);
/* 100:126 */             break;
/* 101:    */           case 1: 
/* 102:128 */             this.status = 2;
/* 103:129 */             statusStack.addFirst(new Integer(this.status));
/* 104:130 */             valueStack.addFirst(createObjectContainer(containerFactory));
/* 105:131 */             break;
/* 106:    */           case 3: 
/* 107:133 */             this.status = 3;
/* 108:134 */             statusStack.addFirst(new Integer(this.status));
/* 109:135 */             valueStack.addFirst(createArrayContainer(containerFactory));
/* 110:136 */             break;
/* 111:    */           case 2: 
/* 112:    */           default: 
/* 113:138 */             this.status = -1;
/* 114:    */           }
/* 115:140 */           break;
/* 116:    */         case 1: 
/* 117:143 */           if (this.token.type == -1) {
/* 118:144 */             return valueStack.removeFirst();
/* 119:    */           }
/* 120:146 */           throw new ParseException(getPosition(), 1, this.token);
/* 121:    */         case 2: 
/* 122:149 */           switch (this.token.type)
/* 123:    */           {
/* 124:    */           case 5: 
/* 125:    */             break;
/* 126:    */           case 0: 
/* 127:153 */             if ((this.token.value instanceof String))
/* 128:    */             {
/* 129:154 */               String key = (String)this.token.value;
/* 130:155 */               valueStack.addFirst(key);
/* 131:156 */               this.status = 4;
/* 132:157 */               statusStack.addFirst(new Integer(this.status));
/* 133:    */             }
/* 134:    */             else
/* 135:    */             {
/* 136:160 */               this.status = -1;
/* 137:    */             }
/* 138:162 */             break;
/* 139:    */           case 2: 
/* 140:164 */             if (valueStack.size() > 1)
/* 141:    */             {
/* 142:165 */               statusStack.removeFirst();
/* 143:166 */               valueStack.removeFirst();
/* 144:167 */               this.status = peekStatus(statusStack);
/* 145:    */             }
/* 146:    */             else
/* 147:    */             {
/* 148:170 */               this.status = 1;
/* 149:    */             }
/* 150:172 */             break;
/* 151:    */           default: 
/* 152:174 */             this.status = -1;
/* 153:    */           }
/* 154:175 */           break;
/* 155:    */         case 4: 
/* 156:    */           String key;
/* 157:    */           Map parent;
/* 158:180 */           switch (this.token.type)
/* 159:    */           {
/* 160:    */           case 6: 
/* 161:    */             break;
/* 162:    */           case 0: 
/* 163:184 */             statusStack.removeFirst();
/* 164:185 */             key = (String)valueStack.removeFirst();
/* 165:186 */             parent = (Map)valueStack.getFirst();
/* 166:187 */             parent.put(key, this.token.value);
/* 167:188 */             this.status = peekStatus(statusStack);
/* 168:189 */             break;
/* 169:    */           case 3: 
/* 170:191 */             statusStack.removeFirst();
/* 171:192 */             key = (String)valueStack.removeFirst();
/* 172:193 */             parent = (Map)valueStack.getFirst();
/* 173:194 */             List newArray = createArrayContainer(containerFactory);
/* 174:195 */             parent.put(key, newArray);
/* 175:196 */             this.status = 3;
/* 176:197 */             statusStack.addFirst(new Integer(this.status));
/* 177:198 */             valueStack.addFirst(newArray);
/* 178:199 */             break;
/* 179:    */           case 1: 
/* 180:201 */             statusStack.removeFirst();
/* 181:202 */             key = (String)valueStack.removeFirst();
/* 182:203 */             parent = (Map)valueStack.getFirst();
/* 183:204 */             Map newObject = createObjectContainer(containerFactory);
/* 184:205 */             parent.put(key, newObject);
/* 185:206 */             this.status = 2;
/* 186:207 */             statusStack.addFirst(new Integer(this.status));
/* 187:208 */             valueStack.addFirst(newObject);
/* 188:209 */             break;
/* 189:    */           case 2: 
/* 190:    */           case 4: 
/* 191:    */           case 5: 
/* 192:    */           default: 
/* 193:211 */             this.status = -1;
/* 194:    */           }
/* 195:213 */           break;
/* 196:    */         case 3: 
/* 197:    */           List val;
/* 198:216 */           switch (this.token.type)
/* 199:    */           {
/* 200:    */           case 5: 
/* 201:    */             break;
/* 202:    */           case 0: 
/* 203:220 */             val = (List)valueStack.getFirst();
/* 204:221 */             val.add(this.token.value);
/* 205:222 */             break;
/* 206:    */           case 4: 
/* 207:224 */             if (valueStack.size() > 1)
/* 208:    */             {
/* 209:225 */               statusStack.removeFirst();
/* 210:226 */               valueStack.removeFirst();
/* 211:227 */               this.status = peekStatus(statusStack);
/* 212:    */             }
/* 213:    */             else
/* 214:    */             {
/* 215:230 */               this.status = 1;
/* 216:    */             }
/* 217:232 */             break;
/* 218:    */           case 1: 
/* 219:234 */             val = (List)valueStack.getFirst();
/* 220:235 */             Map newObject = createObjectContainer(containerFactory);
/* 221:236 */             val.add(newObject);
/* 222:237 */             this.status = 2;
/* 223:238 */             statusStack.addFirst(new Integer(this.status));
/* 224:239 */             valueStack.addFirst(newObject);
/* 225:240 */             break;
/* 226:    */           case 3: 
/* 227:242 */             val = (List)valueStack.getFirst();
/* 228:243 */             List newArray = createArrayContainer(containerFactory);
/* 229:244 */             val.add(newArray);
/* 230:245 */             this.status = 3;
/* 231:246 */             statusStack.addFirst(new Integer(this.status));
/* 232:247 */             valueStack.addFirst(newArray);
/* 233:248 */             break;
/* 234:    */           case 2: 
/* 235:    */           default: 
/* 236:250 */             this.status = -1;
/* 237:    */           }
/* 238:252 */           break;
/* 239:    */         case -1: 
/* 240:254 */           throw new ParseException(getPosition(), 1, this.token);
/* 241:    */         }
/* 242:256 */         if (this.status == -1) {
/* 243:257 */           throw new ParseException(getPosition(), 1, this.token);
/* 244:    */         }
/* 245:259 */       } while (this.token.type != -1);
/* 246:    */     }
/* 247:    */     catch (IOException ie)
/* 248:    */     {
/* 249:262 */       throw ie;
/* 250:    */     }
/* 251:265 */     throw new ParseException(getPosition(), 1, this.token);
/* 252:    */   }
/* 253:    */   
/* 254:    */   private void nextToken()
/* 255:    */     throws ParseException, IOException
/* 256:    */   {
/* 257:269 */     this.token = this.lexer.yylex();
/* 258:270 */     if (this.token == null) {
/* 259:271 */       this.token = new Yytoken(-1, null);
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   private Map createObjectContainer(ContainerFactory containerFactory)
/* 264:    */   {
/* 265:275 */     if (containerFactory == null) {
/* 266:276 */       return new JSONObject();
/* 267:    */     }
/* 268:277 */     Map m = containerFactory.createObjectContainer();
/* 269:279 */     if (m == null) {
/* 270:280 */       return new JSONObject();
/* 271:    */     }
/* 272:281 */     return m;
/* 273:    */   }
/* 274:    */   
/* 275:    */   private List createArrayContainer(ContainerFactory containerFactory)
/* 276:    */   {
/* 277:285 */     if (containerFactory == null) {
/* 278:286 */       return new JSONArray();
/* 279:    */     }
/* 280:287 */     List l = containerFactory.creatArrayContainer();
/* 281:289 */     if (l == null) {
/* 282:290 */       return new JSONArray();
/* 283:    */     }
/* 284:291 */     return l;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void parse(String s, ContentHandler contentHandler)
/* 288:    */     throws ParseException
/* 289:    */   {
/* 290:295 */     parse(s, contentHandler, false);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void parse(String s, ContentHandler contentHandler, boolean isResume)
/* 294:    */     throws ParseException
/* 295:    */   {
/* 296:299 */     StringReader in = new StringReader(s);
/* 297:    */     try
/* 298:    */     {
/* 299:301 */       parse(in, contentHandler, isResume);
/* 300:    */     }
/* 301:    */     catch (IOException ie)
/* 302:    */     {
/* 303:307 */       throw new ParseException(-1, 2, ie);
/* 304:    */     }
/* 305:    */   }
/* 306:    */   
/* 307:    */   public void parse(Reader in, ContentHandler contentHandler)
/* 308:    */     throws IOException, ParseException
/* 309:    */   {
/* 310:312 */     parse(in, contentHandler, false);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void parse(Reader in, ContentHandler contentHandler, boolean isResume)
/* 314:    */     throws IOException, ParseException
/* 315:    */   {
/* 316:330 */     if (!isResume)
/* 317:    */     {
/* 318:331 */       reset(in);
/* 319:332 */       this.handlerStatusStack = new LinkedList();
/* 320:    */     }
/* 321:335 */     else if (this.handlerStatusStack == null)
/* 322:    */     {
/* 323:336 */       isResume = false;
/* 324:337 */       reset(in);
/* 325:338 */       this.handlerStatusStack = new LinkedList();
/* 326:    */     }
/* 327:342 */     LinkedList statusStack = this.handlerStatusStack;
/* 328:    */     try
/* 329:    */     {
/* 330:    */       do
/* 331:    */       {
/* 332:346 */         switch (this.status)
/* 333:    */         {
/* 334:    */         case 0: 
/* 335:348 */           contentHandler.startJSON();
/* 336:349 */           nextToken();
/* 337:350 */           switch (this.token.type)
/* 338:    */           {
/* 339:    */           case 0: 
/* 340:352 */             this.status = 1;
/* 341:353 */             statusStack.addFirst(new Integer(this.status));
/* 342:354 */             if (!contentHandler.primitive(this.token.value)) {
/* 343:    */               return;
/* 344:    */             }
/* 345:    */             break;
/* 346:    */           case 1: 
/* 347:358 */             this.status = 2;
/* 348:359 */             statusStack.addFirst(new Integer(this.status));
/* 349:360 */             if (!contentHandler.startObject()) {
/* 350:    */               return;
/* 351:    */             }
/* 352:    */             break;
/* 353:    */           case 3: 
/* 354:364 */             this.status = 3;
/* 355:365 */             statusStack.addFirst(new Integer(this.status));
/* 356:366 */             if (!contentHandler.startArray()) {
/* 357:    */               return;
/* 358:    */             }
/* 359:    */             break;
/* 360:    */           case 2: 
/* 361:    */           default: 
/* 362:370 */             this.status = -1;
/* 363:    */           }
/* 364:372 */           break;
/* 365:    */         case 1: 
/* 366:375 */           nextToken();
/* 367:376 */           if (this.token.type == -1)
/* 368:    */           {
/* 369:377 */             contentHandler.endJSON();
/* 370:378 */             this.status = 6;
/* 371:379 */             return;
/* 372:    */           }
/* 373:382 */           this.status = -1;
/* 374:383 */           throw new ParseException(getPosition(), 1, this.token);
/* 375:    */         case 2: 
/* 376:387 */           nextToken();
/* 377:388 */           switch (this.token.type)
/* 378:    */           {
/* 379:    */           case 5: 
/* 380:    */             break;
/* 381:    */           case 0: 
/* 382:392 */             if ((this.token.value instanceof String))
/* 383:    */             {
/* 384:393 */               String key = (String)this.token.value;
/* 385:394 */               this.status = 4;
/* 386:395 */               statusStack.addFirst(new Integer(this.status));
/* 387:396 */               if (!contentHandler.startObjectEntry(key)) {
/* 388:397 */                 return;
/* 389:    */               }
/* 390:    */             }
/* 391:    */             else
/* 392:    */             {
/* 393:400 */               this.status = -1;
/* 394:    */             }
/* 395:402 */             break;
/* 396:    */           case 2: 
/* 397:404 */             if (statusStack.size() > 1)
/* 398:    */             {
/* 399:405 */               statusStack.removeFirst();
/* 400:406 */               this.status = peekStatus(statusStack);
/* 401:    */             }
/* 402:    */             else
/* 403:    */             {
/* 404:409 */               this.status = 1;
/* 405:    */             }
/* 406:411 */             if (!contentHandler.endObject()) {
/* 407:    */               return;
/* 408:    */             }
/* 409:    */             break;
/* 410:    */           default: 
/* 411:415 */             this.status = -1;
/* 412:    */           }
/* 413:416 */           break;
/* 414:    */         case 4: 
/* 415:421 */           nextToken();
/* 416:422 */           switch (this.token.type)
/* 417:    */           {
/* 418:    */           case 6: 
/* 419:    */             break;
/* 420:    */           case 0: 
/* 421:426 */             statusStack.removeFirst();
/* 422:427 */             this.status = peekStatus(statusStack);
/* 423:428 */             if (!contentHandler.primitive(this.token.value)) {
/* 424:429 */               return;
/* 425:    */             }
/* 426:430 */             if (!contentHandler.endObjectEntry()) {
/* 427:    */               return;
/* 428:    */             }
/* 429:    */             break;
/* 430:    */           case 3: 
/* 431:434 */             statusStack.removeFirst();
/* 432:435 */             statusStack.addFirst(new Integer(5));
/* 433:436 */             this.status = 3;
/* 434:437 */             statusStack.addFirst(new Integer(this.status));
/* 435:438 */             if (!contentHandler.startArray()) {
/* 436:    */               return;
/* 437:    */             }
/* 438:    */             break;
/* 439:    */           case 1: 
/* 440:442 */             statusStack.removeFirst();
/* 441:443 */             statusStack.addFirst(new Integer(5));
/* 442:444 */             this.status = 2;
/* 443:445 */             statusStack.addFirst(new Integer(this.status));
/* 444:446 */             if (!contentHandler.startObject()) {
/* 445:    */               return;
/* 446:    */             }
/* 447:    */             break;
/* 448:    */           case 2: 
/* 449:    */           case 4: 
/* 450:    */           case 5: 
/* 451:    */           default: 
/* 452:450 */             this.status = -1;
/* 453:    */           }
/* 454:452 */           break;
/* 455:    */         case 5: 
/* 456:459 */           statusStack.removeFirst();
/* 457:460 */           this.status = peekStatus(statusStack);
/* 458:461 */           if (!contentHandler.endObjectEntry()) {
/* 459:    */             return;
/* 460:    */           }
/* 461:    */           break;
/* 462:    */         case 3: 
/* 463:466 */           nextToken();
/* 464:467 */           switch (this.token.type)
/* 465:    */           {
/* 466:    */           case 5: 
/* 467:    */             break;
/* 468:    */           case 0: 
/* 469:471 */             if (!contentHandler.primitive(this.token.value)) {
/* 470:    */               return;
/* 471:    */             }
/* 472:    */             break;
/* 473:    */           case 4: 
/* 474:475 */             if (statusStack.size() > 1)
/* 475:    */             {
/* 476:476 */               statusStack.removeFirst();
/* 477:477 */               this.status = peekStatus(statusStack);
/* 478:    */             }
/* 479:    */             else
/* 480:    */             {
/* 481:480 */               this.status = 1;
/* 482:    */             }
/* 483:482 */             if (!contentHandler.endArray()) {
/* 484:    */               return;
/* 485:    */             }
/* 486:    */             break;
/* 487:    */           case 1: 
/* 488:486 */             this.status = 2;
/* 489:487 */             statusStack.addFirst(new Integer(this.status));
/* 490:488 */             if (!contentHandler.startObject()) {
/* 491:    */               return;
/* 492:    */             }
/* 493:    */             break;
/* 494:    */           case 3: 
/* 495:492 */             this.status = 3;
/* 496:493 */             statusStack.addFirst(new Integer(this.status));
/* 497:494 */             if (!contentHandler.startArray()) {
/* 498:    */               return;
/* 499:    */             }
/* 500:    */             break;
/* 501:    */           case 2: 
/* 502:    */           default: 
/* 503:498 */             this.status = -1;
/* 504:    */           }
/* 505:500 */           break;
/* 506:    */         case 6: 
/* 507:503 */           return;
/* 508:    */         case -1: 
/* 509:506 */           throw new ParseException(getPosition(), 1, this.token);
/* 510:    */         }
/* 511:508 */         if (this.status == -1) {
/* 512:509 */           throw new ParseException(getPosition(), 1, this.token);
/* 513:    */         }
/* 514:511 */       } while (this.token.type != -1);
/* 515:    */     }
/* 516:    */     catch (IOException ie)
/* 517:    */     {
/* 518:514 */       this.status = -1;
/* 519:515 */       throw ie;
/* 520:    */     }
/* 521:    */     catch (ParseException pe)
/* 522:    */     {
/* 523:518 */       this.status = -1;
/* 524:519 */       throw pe;
/* 525:    */     }
/* 526:    */     catch (RuntimeException re)
/* 527:    */     {
/* 528:522 */       this.status = -1;
/* 529:523 */       throw re;
/* 530:    */     }
/* 531:    */     catch (Error e)
/* 532:    */     {
/* 533:526 */       this.status = -1;
/* 534:527 */       throw e;
/* 535:    */     }
/* 536:530 */     this.status = -1;
/* 537:531 */     throw new ParseException(getPosition(), 1, this.token);
/* 538:    */   }
/* 539:    */ }



/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar

 * Qualified Name:     org.json.simple.parser.JSONParser

 * JD-Core Version:    0.7.0.1

 */