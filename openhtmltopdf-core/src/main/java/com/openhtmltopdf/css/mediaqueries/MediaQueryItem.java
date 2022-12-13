package com.openhtmltopdf.css.mediaqueries;

import java.util.List;

import com.openhtmltopdf.layout.SharedContext;
import com.openhtmltopdf.util.GeneralUtil;

public class MediaQueryItem 
{
	public static enum MediaQueryQualifier
	{
		NOT,
		ONLY,
		NONE;
	}
	
	private final MediaQueryQualifier qualifier;

	// The media type defaults to 'all' if left out.
	private final String mediaType;;

	// Expressions in a media query are combined with AND semantics.
	private final List<MediaQueryExpression> expressions;
	
	public MediaQueryItem(MediaQueryQualifier qualifier, String medium, List<MediaQueryExpression> expressions) 
	{
		this.qualifier = qualifier;
		this.mediaType = medium;
		this.expressions = expressions;
	}

	@Override
	public String toString() 
	{
		return "Qualifier: " + qualifier.toString() + ", Media Type: " + mediaType +
				", Expressions: " + expressions.toString();
	}
	
	public boolean eval(SharedContext ctx)
	{
		boolean res = false;
		
		// Webkit uses a case insensitive compare.
		if (GeneralUtil.ciEquals(mediaType, "all") ||
			GeneralUtil.ciEquals(mediaType, ctx.getMedia()) ||
			GeneralUtil.ciEquals(ctx.getMedia(), "all"))
		{
			res = true;
		}

		if (res == true)
		{
			for (MediaQueryExpression expr : expressions)
			{
				if (!expr.eval(ctx))
					res = false;
			}
		}
		
		if (qualifier == MediaQueryQualifier.NOT)
			res = !res;

		return res;
	}
}
