package com.openhtmltopdf.css.mediaqueries;

import java.util.Arrays;
import java.util.List;

import com.openhtmltopdf.css.constants.IdentValue;
import com.openhtmltopdf.css.parser.CSSPrimitiveValue;
import com.openhtmltopdf.css.parser.PropertyValue;
import com.openhtmltopdf.css.parser.Token;
import com.openhtmltopdf.layout.SharedContext;

public class MediaQueryExpression
{
	private final MediaFeatureName _mediaFeature;
	private final PropertyValue _cssValue;
	private final boolean _isValid;
	
	public boolean isValid()
	{
		return _isValid;
	}
	
	public MediaFeatureName mediaFeature()
	{
		return _mediaFeature;
	}
	
	public PropertyValue value()
	{
		return _cssValue;
	}
	
	public boolean isViewportDependent()
	{
		return _mediaFeature == MediaFeatureName.WIDTH
				|| _mediaFeature == MediaFeatureName.HEIGHT
				|| _mediaFeature == MediaFeatureName.MIN_WIDTH
				|| _mediaFeature == MediaFeatureName.MIN_HEIGHT
				|| _mediaFeature == MediaFeatureName.MAX_WIDTH
				|| _mediaFeature == MediaFeatureName.MAX_HEIGHT
				|| _mediaFeature == MediaFeatureName.ORIENTATION
				|| _mediaFeature == MediaFeatureName.ASPECT_RATIO
				|| _mediaFeature == MediaFeatureName.MIN_ASPECT_RATIO
				|| _mediaFeature == MediaFeatureName.MAX_ASPECT_RATIO;
	}
	
	private static boolean featureWithCSSValueID(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		final IdentValue result = IdentValue.valueOf(value.getStringValue());
		
		if (result == null)
			return false;
		
		return mediaFeature == MediaFeatureName.ORIENTATION
				|| mediaFeature == MediaFeatureName.POINTER;
	}
	
	private static boolean featureWithValidPositiveLenghtOrNumber(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		if (!isLength(value) && value.getPrimitiveType() != CSSPrimitiveValue.CSS_PERCENTAGE)
			return false;
		
		if (value.getFloatValue() < 0)
			return false;
		
		return mediaFeature == MediaFeatureName.HEIGHT
				|| mediaFeature == MediaFeatureName.MAX_HEIGHT
				|| mediaFeature == MediaFeatureName.MIN_HEIGHT
				|| mediaFeature == MediaFeatureName.WIDTH
				|| mediaFeature == MediaFeatureName.MAX_WIDTH
				|| mediaFeature == MediaFeatureName.MIN_WIDTH
				|| mediaFeature == MediaFeatureName.DEVICE_HEIGHT
				|| mediaFeature == MediaFeatureName.MAX_DEVICE_HEIGHT
				|| mediaFeature == MediaFeatureName.MIN_DEVICE_HEIGHT
				|| mediaFeature == MediaFeatureName.DEVICE_WIDTH
				|| mediaFeature == MediaFeatureName.MAX_DEVICE_WIDTH
				|| mediaFeature == MediaFeatureName.MIN_DEVICE_WIDTH;
	}
	
	public static boolean isLength(final PropertyValue value) {
		final short unit = value.getPrimitiveType();
		return unit == CSSPrimitiveValue.CSS_EMS
				|| unit == CSSPrimitiveValue.CSS_EXS
				|| unit == CSSPrimitiveValue.CSS_PX
				|| unit == CSSPrimitiveValue.CSS_IN
				|| unit == CSSPrimitiveValue.CSS_CM
				|| unit == CSSPrimitiveValue.CSS_MM
				|| unit == CSSPrimitiveValue.CSS_PT
				|| unit == CSSPrimitiveValue.CSS_PC
				|| (unit == CSSPrimitiveValue.CSS_NUMBER && value
					.getFloatValue() == 0.0f);
	}
	
	private static boolean featureWithValidDensity(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		if ((value.getPrimitiveType() != CSSPrimitiveValue.CSS_DPPX && value.getPrimitiveType() != CSSPrimitiveValue.CSS_DPI && value.getPrimitiveType() != CSSPrimitiveValue.CSS_DPCM) || value.getFloatValue() <= 0)
			return false;
		
		return mediaFeature == MediaFeatureName.RESOLUTION
				|| mediaFeature == MediaFeatureName.MAX_RESOLUTION
				|| mediaFeature == MediaFeatureName.MIN_RESOLUTION;
	}
	
	private static boolean featureWithPositiveInteger(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		if ((value.getFloatValue() % 1f) != 0f || value.getFloatValue() < 0)
			return false;
		
		return mediaFeature == MediaFeatureName.COLOR
				|| mediaFeature == MediaFeatureName.MAX_COLOR
				|| mediaFeature == MediaFeatureName.MIN_COLOR
				|| mediaFeature == MediaFeatureName.COLOR_INDEX
				|| mediaFeature == MediaFeatureName.MAX_COLOR_INDEX
				|| mediaFeature == MediaFeatureName.MIN_COLOR_INDEX
				|| mediaFeature == MediaFeatureName.MIN_MONOCHROME
				|| mediaFeature == MediaFeatureName.MAX_MONOCHROME;
	}
	
	private static boolean featureWithPositiveNumber(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		if (value.getPrimitiveType() != CSSPrimitiveValue.CSS_NUMBER || value.getFloatValue() < 0)
			return false;
		
		// No non-webkit specific media expressions fit this cirteria.
		return false;
	}
	
	private static boolean featureWithZeroOrOne(final MediaFeatureName mediaFeature, final PropertyValue value)
	{
		if ((value.getFloatValue() % 1f) != 0f || !(value.getFloatValue() == 1 || value.getFloatValue() == 0))
			return false;
		
		return mediaFeature == MediaFeatureName.GRID
				|| mediaFeature == MediaFeatureName.HOVER;
	}
	
	private static boolean featureWithAspectRatio(final MediaFeatureName mediaFeature)
	{
		return mediaFeature == MediaFeatureName.ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.DEVICE_ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.MIN_ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.MAX_ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.MIN_DEVICE_ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.MAX_DEVICE_ASPECT_RATIO;
	}
	
	private static boolean featureWithoutValue(final MediaFeatureName mediaFeature)
	{
		// Media features that are prefixed by min/max cannot be used without a value.
		return mediaFeature == MediaFeatureName.MONOCHROME
				|| mediaFeature == MediaFeatureName.COLOR
				|| mediaFeature == MediaFeatureName.COLOR_INDEX
				|| mediaFeature == MediaFeatureName.GRID
				|| mediaFeature == MediaFeatureName.HEIGHT
				|| mediaFeature == MediaFeatureName.WIDTH
				|| mediaFeature == MediaFeatureName.DEVICE_HEIGHT
				|| mediaFeature == MediaFeatureName.DEVICE_WIDTH
				|| mediaFeature == MediaFeatureName.ORIENTATION
				|| mediaFeature == MediaFeatureName.ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.DEVICE_ASPECT_RATIO
				|| mediaFeature == MediaFeatureName.HOVER
				|| mediaFeature == MediaFeatureName.POINTER
				|| mediaFeature == MediaFeatureName.RESOLUTION;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder result = new StringBuilder();
		result.append('(');
		result.append(_mediaFeature.toString());
		if (_cssValue != null)
		{
			result.append(": ");
			result.append(_cssValue.getCssText());
		}
		result.append(')');
		
		return result.toString();
	}
	
	public MediaQueryExpression(final MediaFeatureName mediaFeatureStr, final List<PropertyValue> valueList)
	{
		_mediaFeature = mediaFeatureStr;
		
		// Initialize media query expression that must have 1 or more values.
		if ((valueList == null || valueList.isEmpty()) &&
				featureWithoutValue(_mediaFeature))
		{
			_isValid = true;
			_cssValue = null;
			return;
		}
		else if (valueList == null)
		{
			_isValid = false;
			_cssValue = null;
			return;
		}
		
		if (valueList.size() == 1)
		{
			_cssValue = valueList.get(0);
			
			if (featureWithCSSValueID(_mediaFeature, _cssValue) ||
					featureWithValidDensity(_mediaFeature, _cssValue) ||
					featureWithValidPositiveLenghtOrNumber(_mediaFeature, _cssValue) ||
					featureWithPositiveInteger(_mediaFeature, _cssValue) ||
					featureWithPositiveNumber(_mediaFeature, _cssValue) ||
					featureWithZeroOrOne(_mediaFeature, _cssValue))
			{
				_isValid = true;
				return;
			}
		}
		else if (valueList.size() == 2 && featureWithAspectRatio(_mediaFeature))
		{
			final PropertyValue top = valueList.get(0);
			final PropertyValue bottom = valueList.get(1);
			
			final List<PropertyValue> values = Arrays.asList(top, bottom);
			
			_cssValue = new PropertyValue(values);
			
			// The aspect-ratio must be <integer> (whitespace)? / (whitespace)? <integer>.
			if (top.getFloatValue() % 1f == 0f &&
					bottom.getFloatValue() % 1f == 0f &&
					bottom.getOperator() == Token.TK_VIRGULE)
			{
				_isValid = true;
				return;
			}
		}
		else
		{
			_cssValue = null;
		}
		
		_isValid = false;
	}
	
	public boolean eval(SharedContext ctx)
	{
		if (_isValid)
			return mediaFeature().eval(ctx, this._cssValue);
		else
			return false;
	}
}
