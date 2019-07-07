package biz.orgin.minecraft.hothgenerator;

import org.bukkit.ChatColor;

public class MessageFormatter
{
	public static String format(String message)
	{
		StringBuffer sb = new StringBuffer();
		int mode = 0;
		for(int i=0;i<message.length();i++)
		{
			char c = message.charAt(i);
			
			switch(mode)
			{
			case 0:
				if(c=='&')
				{
					mode = 1;
				}
				else
				{
					sb.append(c);
				}
				break;
			case 1:
				if( (c>='0' && c<='9') || (c>='a' && c<='f')
						|| c=='k' || c=='l' || c=='m' || c=='n' || c=='o' || c=='r')
				{
					ChatColor cc = ChatColor.getByChar(c);
					sb.append(cc.toString());
					mode = 0;
				}
				else
				{
					sb.append("&");
					sb.append(c);
					mode = 0;
				}
			}
		}
		
		return sb.toString();
	}
}
