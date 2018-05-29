package edu.iis.powp.adapter;

import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.observer.Subscriber;
import edu.kis.powp.drawer.shape.ILine;
import edu.kis.powp.drawer.shape.LineFactory;

import javax.imageio.event.IIOReadProgressListener;

import static edu.iis.powp.adapter.InkController.operationType.drawpos;
import static edu.iis.powp.adapter.InkController.operationType.setpos;

public class InkController implements IPlotter, InkGuiUpdater{

    private ILine line = LineFactory.getBasicLine();
    IPlotter plotter;
    InkGui inkGui = InkGui.getInstance();

    @Override
    public void updateValue(float value) {
        amountOfInk = value;
        inkGui.updateValue(amountOfInk);
    }

    public enum operationType{
        setpos,
        drawpos
    }
    operationType opType;

    float amountOfInk;
    double posX, posY;

    public InkController(IPlotter plotter, float amountOfInk){
        this.plotter = plotter;
        this.amountOfInk = amountOfInk;
    }

    @Override
    public void setPosition(int x, int y)
    {
        plotter.setPosition(x, y);

        posX = x;
        posY = y;
        opType = setpos;
        System.out.println("Int controller adapter - " + opType);
    }

    @Override
    public void drawTo(int x, int y)
    {
        opType = drawpos;
        System.out.println("Ink controller adapter - " + opType);
        if(opType == drawpos)
        {
            amountOfInk -= Math.sqrt(Math.pow((posX - x), 2) + Math.pow(posY - y, 2));
        }
        posX = x;
        posY = y;
        System.out.println("Amount of ink - " + amountOfInk);
        if (amountOfInk < 0)
        {
            if(plotter.toString().equals("Basic plotter"))
            {
                ((LineAdapterPlotterDriver)plotter).setLine(LineFactory.getDottedLine());
            }
        }
        plotter.drawTo(x, y);
        inkGui.updateValue(amountOfInk);
    }


}