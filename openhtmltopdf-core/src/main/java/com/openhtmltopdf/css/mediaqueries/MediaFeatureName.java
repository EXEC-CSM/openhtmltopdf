package com.openhtmltopdf.css.mediaqueries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openhtmltopdf.css.parser.CSSPrimitiveValue;
import com.openhtmltopdf.css.parser.CSSValue;
import com.openhtmltopdf.css.parser.PropertyValue;
import com.openhtmltopdf.css.style.derived.LengthValue;
import com.openhtmltopdf.layout.SharedContext;

public enum MediaFeatureName
{
	COLOR("color") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			// Indicates the number of bits per color component
			// of the output device.
			// We assume we only support color and 32bit colors.
			if (_cssValue == null)
				return true;
			else
				return evalNumber(_cssValue, 8f, '=');
		}
	},
	COLOR_INDEX("color-index") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			// We don't support indexed colors, so only color-index: 0 should
			// return true.
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '=');
		}
	},
	GRID("grid") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			// We are a bitmap device rather than a grid device, so only
			// grid: 0 should return true.
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '=');
		}
	},
	MONOCHROME("monochrome") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			// We don't support monochrome, so only monochrome: 0 should
			// return true.
			if (_cssValue == null)
				return false;
			else 
				return evalNumber(_cssValue, 0f, '=');
		}
	},
	HEIGHT("height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (pixels == ctx.getFixedRectangle().height)
				return true;
			
			return false;
		}
	},
	HOVER("hover") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) {
			// We're a static renderer so don't support hover.
			return false;
		}
	},
	WIDTH("width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (pixels == ctx.getFixedRectangle().width)
				return true;
			
			return false;
		}
	},
	ORIENTATION("orientation") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			boolean portrait = false;

			if (ctx.getFixedRectangle().height > ctx.getFixedRectangle().width)
				portrait = true;
			
			String test = _cssValue.getStringValue();
			
			if ("portrait".equals(test))
				return portrait;
			else if ("landscape".equals(test))
				return !portrait;
			else
				return false;
		}
	},
	ASPECT_RATIO("aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getFixedRectangle().width / (float) ctx.getFixedRectangle().height;
			return desiredAspectRatio == actualAspectRatio;
		}
	},
	DEVICE_ASPECT_RATIO("device-aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getDeviceDimension().width / (float) ctx.getDeviceDimension().height;
			return desiredAspectRatio == actualAspectRatio;
		}
	},
	DEVICE_HEIGHT("device-height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (pixels == ctx.getDeviceDimension().height)
				return true;
			
			return false;
		}
	},
	DEVICE_WIDTH("device-width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (pixels == ctx.getDeviceDimension().width)
				return true;
			
			return false;
		}
	},
	MAX_COLOR("max-color") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return true;
			else 
				return evalNumber(_cssValue, 8f, '<');
		}
	},
	MAX_COLOR_INDEX("max-color-index") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '<');
		}
	},
	MAX_ASPECT_RATIO("max-aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getFixedRectangle().width / (float) ctx.getFixedRectangle().height;
			return actualAspectRatio <= desiredAspectRatio;
		}
	},
	MAX_DEVICE_ASPECT_RATIO("max-device-aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getDeviceDimension().width / (float) ctx.getDeviceDimension().height;
			return actualAspectRatio >= desiredAspectRatio;
		}
	},
	MAX_DEVICE_HEIGHT("max-device-height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (ctx.getDeviceDimension().height <= pixels)
				return true;
			
			return false;
		}
	},
	MAX_DEVICE_WIDTH("max-device-width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (ctx.getDeviceDimension().width <= pixels)
				return true;
				
			return false;
		}
	},
	MAX_HEIGHT("max-height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
				
			if (ctx.getFixedRectangle().height <= pixels)
				return true;
				
			return false;
		}
	},
	MAX_MONOCHROME("max-monochrome") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '<');
		}
	},
	MAX_WIDTH("max-width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
				
			if (ctx.getFixedRectangle().width <= pixels)
				return true;
				
			return false;
		}
	},
	MAX_RESOLUTION("max-resolution") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float actualResolution = ctx.getDotsPerPixel();
			float desiredResolution = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (actualResolution <= desiredResolution)
				return true;
			
			return false;
		}
	},
	MIN_COLOR("min-color") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return true;
			else
				return evalNumber(_cssValue, 8f, '>');
		}
	},
	MIN_COLOR_INDEX("min-color-index") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '>');
		}
	},
	MIN_ASPECT_RATIO("min-aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getFixedRectangle().width / (float) ctx.getFixedRectangle().height;
			return actualAspectRatio >= desiredAspectRatio;
		}
	},
	MIN_DEVICE_ASPECT_RATIO("min-device-aspect-ratio") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float desiredAspectRatio = calcDesiredRatio(_cssValue);
			float actualAspectRatio = (float) ctx.getDeviceDimension().width / (float) ctx.getDeviceDimension().height;
			return actualAspectRatio >= desiredAspectRatio;
		}
	},
	MIN_DEVICE_HEIGHT("min-device-height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (ctx.getDeviceDimension().height >= pixels)
				return true;
			
			return false;
		}
	},
	MIN_DEVICE_WIDTH("min-device-width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (ctx.getDeviceDimension().width >= pixels)
				return true;
				
			return false;
		}
	},
	MIN_HEIGHT("min-height") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
			
			if (ctx.getFixedRectangle().height >= pixels)
				return true;
			
			return false;
		}
	},
	MIN_MONOCHROME("min-monochrome") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			if (_cssValue == null)
				return false;
			else
				return evalNumber(_cssValue, 0f, '>');
		}
	},
	MIN_WIDTH("min-width") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			float pixels = LengthValue.calcFloatProportionalValue(_cssValue, ctx);
				
			if (ctx.getFixedRectangle().width >= pixels)
				return true;
				
			return false;
		}
	},
	MIN_RESOLUTION("min-resolution") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float actualResolution = ctx.getDotsPerPixel();
			float desiredResolution = LengthValue.calcFloatProportionalValue(_cssValue, ctx);

			if (actualResolution >= desiredResolution)
				return true;
			
			return false;
		}
	},
	POINTER("pointer") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue)
		{
			// Webkit says:
		    // If we're on a port that hasn't explicitly opted into providing pointer device information
		    // (or otherwise can't be confident in the pointer hardware available), then behave exactly
		    // as if this feature feature isn't supported.
			return false;
		}
	},
	RESOLUTION("resolution") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			float actualResolution = ctx.getDotsPerPixel();
			float desiredResolution = LengthValue.calcFloatProportionalValue(_cssValue, ctx);

			if (actualResolution == desiredResolution)
				return true;
			
			return false;
		}
	},
	SCAN("scan") {
		@Override
		public boolean eval(SharedContext ctx, PropertyValue _cssValue) 
		{
			// We don't support this scan media (such as TV).
			return false;
		}
	};
	
	private final String cssName;
	
	private MediaFeatureName(final String name)
	{
		cssName = name;
	}

	@Override
	public String toString() 
	{
		return cssName;
	}

	private static final Map<String, MediaFeatureName> map = new HashMap<String, MediaFeatureName>(values().length);
	
	static 
	{
		for (final MediaFeatureName nm : values())
		{
			map.put(nm.cssName, nm);
		}
	}
	
	public static MediaFeatureName fsValueOf(final String mediaFeatureStr)
	{
		return map.get(mediaFeatureStr);
	}
	
	public static boolean isRatio(final String mediaFeatureString)
	{
		MediaFeatureName mediaFeature = fsValueOf(mediaFeatureString);

		return mediaFeature == MediaFeatureName.ASPECT_RATIO
                || mediaFeature == MediaFeatureName.DEVICE_ASPECT_RATIO
                || mediaFeature == MediaFeatureName.MIN_ASPECT_RATIO
                || mediaFeature == MediaFeatureName.MAX_ASPECT_RATIO
                || mediaFeature == MediaFeatureName.MIN_DEVICE_ASPECT_RATIO
                || mediaFeature == MediaFeatureName.MAX_DEVICE_ASPECT_RATIO;
	}

	private static float calcDesiredRatio(PropertyValue val)
	{
		@SuppressWarnings("unchecked")
		List<PropertyValue> values = (List<PropertyValue>) val.getValues();
		
		float width = values.get(0).getFloatValue();
		float height = values.get(1).getFloatValue();

		if (height == 0)
			return 0f;
		
		float desiredAspectRatio = width / height;
		
		return desiredAspectRatio;
	}
	
	private static boolean evalNumber(PropertyValue val, float number, char op)
	{
		if (op == '>') // min-prefix (greater or equal to)
		{
			return (val.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE &&
					val.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER &&
					number >= val.getFloatValue());
		}
		else if (op == '<') // max-prefix (less than or equal to)
		{
			return (val.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE &&
					val.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER &&
					number <= val.getFloatValue());	
		}
		else // no-prefix
		{
			assert(op == '=');
			return (val.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE &&
					val.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER &&
					val.getFloatValue() == number);	
		}
	}
	
	public abstract boolean eval(SharedContext ctx, PropertyValue _cssValue); 
}
