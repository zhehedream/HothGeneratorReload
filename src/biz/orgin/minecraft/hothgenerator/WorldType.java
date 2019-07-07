package biz.orgin.minecraft.hothgenerator;

public enum WorldType {
 HOTH ("hoth"),
 TATOOINE ("tatooine"),
 DAGOBAH ("dagobah"),
 MUSTAFAR ("mustafar"),
 KASHYYYK("kashyyyk"),
 KAMINO("kamino");
 
 private final String name;
 
 private WorldType(String name)
 {
	 this.name = name;
 }
 
 public String toString()
 {
	 return name;
 }
 
 public boolean equals(String name)
 {
	 return this.name.equals(name);
 }
 
 public static WorldType getType(String type) throws InvalidWorldTypeException
 {
	 for(WorldType t : WorldType.values())
	 {
		 if(t.name.equals(type.toLowerCase()))
		 {
			 return t;
		 }
	 }
	 
	 throw new InvalidWorldTypeException(type + " is not a valid world type");
 }
 
 public static class InvalidWorldTypeException extends Exception
 {
	private static final long serialVersionUID = 2780389615326711849L;
	
	private String message;

	public InvalidWorldTypeException(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
 }
}
