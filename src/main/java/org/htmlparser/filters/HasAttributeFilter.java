package org.htmlparser.filters;


import java.util.Locale;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;

/**
 * <pre>
 * 拷贝自htmlparser，修复CssExtract时，根据class属性获取相应节点时的bug。
 * 如 &lt;div class="list list-pg"&gt;ttt&lt;/div&gt; ，允许以.list获取到相应css的节点
 * This class accepts all tags that have a certain attribute,
 * and optionally, with a certain value.
 * </pre>
 */
public class HasAttributeFilter implements NodeFilter
{
	private static final long serialVersionUID = -7589726784150483750L;

	/**
     * The attribute to check for.
     */
    protected String mAttribute;

    /**
     * The value to check for.
     */
    protected String mValue;

    /**
     * Creates a new instance of HasAttributeFilter.
     * With no attribute name, this would always return <code>false</code>
     * from {@link #accept}.
     */
    public HasAttributeFilter ()
    {
        this ("", null);
    }

    /**
     * Creates a new instance of HasAttributeFilter that accepts tags
     * with the given attribute.
     * @param attribute The attribute to search for.
     */
    public HasAttributeFilter (String attribute)
    {
        this (attribute, null);
    }

    /**
     * Creates a new instance of HasAttributeFilter that accepts tags
     * with the given attribute and value.
     * @param attribute The attribute to search for.
     * @param value The value that must be matched,
     * or null if any value will match.
     */
    public HasAttributeFilter (String attribute, String value)
    {
        mAttribute = attribute.toUpperCase (Locale.ENGLISH);
        mValue = value;
    }

    /**
     * Get the attribute name.
     * @return Returns the name of the attribute that is acceptable.
     */
    public String getAttributeName ()
    {
        return (mAttribute);
    }

    /**
     * Set the attribute name.
     * @param name The name of the attribute to accept.
     */
    public void setAttributeName (String name)
    {
        mAttribute = name;
    }

    /**
     * Get the attribute value.
     * @return Returns the value of the attribute that is acceptable.
     */
    public String getAttributeValue ()
    {
        return (mValue);
    }

    /**
     * Set the attribute value.
     * @param value The value of the attribute to accept.
     * If <code>null</code>, any tag with the attribute,
     * no matter what it's value is acceptable.
     */
    public void setAttributeValue (String value)
    {
        mValue = value;
    }

    /**
     * Accept tags with a certain attribute.
     * @param node The node to check.
     * @return <code>true</code> if the node has the attribute
     * (and value if that is being checked too), <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        Tag tag;
        Attribute attribute;
        boolean ret;

        ret = false;
        if (node instanceof Tag)
        {
            tag = (Tag)node;
            attribute = tag.getAttributeEx (mAttribute);
            if(attribute != null && attribute.getValue() != null && mValue != null) {
                String[] attrs = attribute.getValue().split(" ");
                for(String attr : attrs) {
                    if(mValue.equals(attr)) {
                        return true;
                    }
                }
                attribute.getValue();
            }
        }

        return (ret);
    }
}
