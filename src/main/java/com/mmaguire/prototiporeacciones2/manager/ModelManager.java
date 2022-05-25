package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.EdgeKind;
import com.mmaguire.prototiporeacciones2.model.LocationKind;
import com.uppaal.engine.*;
import com.uppaal.model.core2.*;
import com.uppaal.model.system.UppaalSystem;
import com.uppaal.model.system.concrete.ConcreteTrace;
import com.uppaal.model.system.symbolic.SymbolicTrace;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

public class ModelManager {

    static SymbolicTrace strace = null;
    static ConcreteTrace ctrace = null;

    /**
     * Sets a label on a location.
     * @param l the location on which the label is going to be attached
     * @param kind a kind of the label
     * @param value the label value (either boolean or String)
     * @param x the x coordinate of the label
     * @param y the y coordinate of the label
     */
    public static void setLabel(Location l, LocationKind kind, Object value, int x, int y) {
        l.setProperty(kind.name(), value);
        Property p = l.getProperty(kind.name());
        p.setProperty("x", x);
        p.setProperty("y", y);
    }

    /**
     * Sets a label on an edge.
     * @param e the edge
     * @param kind the kind of the label
     * @param value the content of the label
     * @param x the x coordinate of the label
     * @param y the y coordinate of the label
     */
    public static void setLabel(Edge e, EdgeKind kind, String value, int x, int y) {
        e.setProperty(kind.name(), value);
        Property p = e.getProperty(kind.name());
        p.setProperty("x", x);
        p.setProperty("y", y);
    }

    /**
     * Adds a location to a template.
     * @param t the template
     * @param name a name for the new location
     * @param exprate an expression for an exponential rate
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return the new location instance
     */
    public static Location addLocation(Template t, String name, String exprate,
                                       int x, int y)
    {
        Location l = t.createLocation();
        t.insert(l, null);
        l.setProperty("x", x);
        l.setProperty("y", y);
        if (name != null)
            setLabel(l, LocationKind.name, name, x, y+15);
        if (exprate != null)
            setLabel(l, LocationKind.exponentialrate, exprate, x, y+35);
        return l;
    }

    /**
     * Adds an edge to the template
     * @param t the template where the edge belongs
     * @param source the source location
     * @param target the target location
     * @param guard guard expression
     * @param sync synchronization expression
     * @param update update expression
     * @return
     */
    public static Edge addEdge(Template t, Location source, Location target,
                               String guard, String sync, String update)
    {
        Edge e = t.createEdge();
        t.insert(e, null);
        e.setSource(source);
        e.setTarget(target);
        int x = (source.getX()+target.getX())/2;
        int y = (source.getY()+target.getY())/2;
        if (guard != null) {
            setLabel(e, EdgeKind.guard, guard, x, y-55);
        }
        if (sync != null) {
            setLabel(e, EdgeKind.synchronisation, sync, x-15, y-14);
        }
        if (update != null) {
            setLabel(e, EdgeKind.assignment, update, x-75, y-25);
        }
        return e;
    }


    public static Engine connectToEngine() throws EngineException, IOException
    {
        String os = System.getProperty("os.name");
        String here = System.getProperty("user.dir");
        String path = null;
        if ("Linux".equals(os)) {
            path = here+"/bin-Linux/server";
        } else if ("Mac OS X".equals(os)) {
            path = here+"/bin-Darwin/server";
        } else if ("Windows".equals(os)) {
            path = here+"\\bin-Windows\\server.exe";
        } else {
            System.err.println("Unknown operating system.");
            System.exit(1);
        }
        Engine engine = new Engine();
        engine.setServerPath(path);
        engine.setServerHost("localhost");
        engine.setConnectionMode(EngineStub.BOTH);
        engine.connect();
        return engine;
    }


    public static UppaalSystem compile(Engine engine, Document doc)
            throws EngineException, IOException
    {
        // compile the model into system:
        ArrayList<Problem> problems = new ArrayList<>();
        UppaalSystem sys = engine.getSystem(doc, problems);
        if (!problems.isEmpty()) {
            boolean fatal = false;
            System.out.println("There are problems with the document:");
            for (Problem p : problems) {
                System.out.println(p.toString());
                if (!"warning".equals(p.getType())) { // ignore warnings
                    fatal = true;
                }
            }
            if (fatal) {
                System.exit(1);
            }
        }
        return sys;
    }

    public static final String options = "--search-order 0 --diagnostic 0";
    // see "verifyta --help" for the description of options
    public static QueryFeedback qf =
            new QueryFeedback() {
                @Override
                public void setProgressAvail(boolean availability)
                {
                }

                @Override
                public void setProgress(int load, long vm, long rss, long cached, long avail, long swap, long swapfree, long user, long sys, long timestamp)
                {
                }

                @Override
                public void setSystemInfo(long vmsize, long physsize, long swapsize)
                {
                }

                @Override
                public void setLength(int length)
                {
                }

                @Override
                public void setCurrent(int pos)
                {
                }

                @Override
                public void setTrace(char result, String feedback,
                                     SymbolicTrace trace, QueryResult queryVerificationResult)
                {
                    strace = trace;
                }

                public void setTrace(char result, String feedback,
                                     ConcreteTrace trace, QueryResult queryVerificationResult)
                {
                    ctrace = trace;
                }
                @Override
                public void setFeedback(String feedback)
                {
                    if (feedback != null && feedback.length() > 0) {
                        System.out.println("Feedback: "+feedback);
                    }
                }

                @Override
                public void appendText(String s)
                {
                    if (s != null && s.length() > 0) {
                        System.out.println("Append: "+s);
                    }
                }

                @Override
                public void setResultText(String s)
                {
                    if (s != null && s.length() > 0) {
                        System.out.println("Result: "+s);
                    }
                }
            };
}
