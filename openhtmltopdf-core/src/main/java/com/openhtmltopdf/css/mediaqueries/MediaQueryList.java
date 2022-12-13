package com.openhtmltopdf.css.mediaqueries;

import java.util.ArrayList;
import java.util.List;

import com.openhtmltopdf.layout.SharedContext;

public class MediaQueryList
{
	// Each media query is combined together with OR semantics.
	private final List<MediaQueryItem> queryItems = new ArrayList<MediaQueryItem>(2);

	public void addMediaQueryItem(MediaQueryItem mediaQuery) 
	{
		queryItems.add(mediaQuery);
	}

	public boolean eval(SharedContext ctx) 
	{
		if (queryItems.isEmpty())
		{
			return true;
		}
		else
		{
			for (MediaQueryItem item : queryItems)
			{
//				FSCancelController.cancelOpportunity(MediaQueryList.class);
				
				if (item.eval(ctx))
					return true;
			}

			return false;
		}
	}
	
	@Override
	public String toString() 
	{
		return queryItems.toString();
	}
}
