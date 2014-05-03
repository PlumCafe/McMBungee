package org.json.simple.parser;

import java.util.List;
import java.util.Map;

public abstract interface ContainerFactory
{
  public abstract Map createObjectContainer();
  
  public abstract List creatArrayContainer();
}


/* Location:           C:\Users\Spyros\Downloads\json-simple-1.1.1.jar
 * Qualified Name:     org.json.simple.parser.ContainerFactory
 * JD-Core Version:    0.7.0.1
 */