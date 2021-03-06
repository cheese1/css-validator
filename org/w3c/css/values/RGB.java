//
// Rewritten in 2018: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM, Keio University, Beihang University 2001-2018.
// Please first read the full copyright statement in file COPYRIGHT.html
//
package org.w3c.css.values;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.CssVersion;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.util.Util;

public class RGB {
    static final String functionname = "rgb";

    private String output = null;
    private boolean percent = false;

    CssValue vr, vg, vb;

    public static final CssValue filterValue(ApplContext ac, CssValue val)
            throws InvalidParamException {
        if (val.getRawType() == CssTypes.CSS_CALC) {
            // TODO add warning about uncheckability
            // might need to extend...
        } else {
            if (val.getType() == CssTypes.CSS_NUMBER) {
                boolean isCss3 = (ac.getCssVersion().compareTo(CssVersion.CSS3) >= 0);
                CssCheckableValue v = val.getCheckableValue();
                // in CSS2, numbers can only be integers
                if (!isCss3) {
                    if (!v.isInteger()) {
                        throw new InvalidParamException("rgb", val, ac);
                    }
                }
                if (!v.warnPositiveness(ac, "RGB")) {
                    CssNumber nb = new CssNumber();
                    nb.setIntValue(0);
                    return nb;
                }
                if (val.getRawType() == CssTypes.CSS_NUMBER) {
                    float p = ((CssNumber) val).getValue();
                    if (p > 255.) {
                        ac.getFrame().addWarning("out-of-range", Util.displayFloat(p));
                        CssNumber nb = new CssNumber();
                        nb.setIntValue(255);
                        return nb;
                    }
                }
            } else if (val.getType() == CssTypes.CSS_PERCENTAGE) {
                CssCheckableValue v = val.getCheckableValue();
                if (!v.warnPositiveness(ac, "RGB")) {
                    CssNumber nb = new CssNumber();
                    nb.setIntValue(0);
                    return nb;
                }
                if (val.getRawType() == CssTypes.CSS_PERCENTAGE) {
                    float p = ((CssPercentage) val).floatValue();
                    if (p > 100.) {
                        ac.getFrame().addWarning("out-of-range", Util.displayFloat(p));
                        return new CssPercentage(100);
                    }
                }
            }
        }
        return val;
    }

    public final void setRed(ApplContext ac, CssValue val)
            throws InvalidParamException {
        output = null;
        vr = filterValue(ac, val);
    }

    public final void setGreen(ApplContext ac, CssValue val)
            throws InvalidParamException {
        output = null;
        vg = filterValue(ac, val);
    }


    public final void setBlue(ApplContext ac, CssValue val)
            throws InvalidParamException {
        output = null;
        vb = filterValue(ac, val);
    }

    /**
     * @return Returns the percent.
     */
    public final boolean isPercent() {
        return percent;
    }

    /**
     * @param percent The percent to set.
     */
    public final void setPercent(boolean percent) {
        this.percent = percent;
    }

    /**
     * Create a new RGB
     */
    public RGB() {
    }

    /**
     * Create a new RGB with default values
     *
     * @param r the red channel, an <EM>int</EM>
     * @param g the green channel, an <EM>int</EM>
     * @param b the blue channel, an <EM>int</EM>
     */
    public RGB(int r, int g, int b) {
        CssNumber n = new CssNumber();
        n.setIntValue(r);
        vr = n;
        n = new CssNumber();
        n.setIntValue(g);
        vg = n;
        n = new CssNumber();
        n.setIntValue(b);
        vb = n;
    }

    public boolean equals(RGB other) {
        if (other != null) {
            return (vr.equals(other.vr) && vg.equals(other.vg) && vb.equals(other.vb));
        }
        return false;
    }

    protected void setRepresentationString(String s) {
        output = s;
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString() {
        if (output == null) {
            StringBuilder sb = new StringBuilder(functionname).append('(');
            sb.append(vr).append(", ");
            sb.append(vg).append(", ");
            sb.append(vb).append(')');
            output = sb.toString();
        }
        return output;
    }
}
