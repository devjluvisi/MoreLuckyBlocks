package devjluvisi.mlb.helper;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.Range;

import java.util.ArrayList;

public class BlockScheduler {


    private MoreLuckyBlocks plugin;
    private ArrayList<Range> coords;

    public BlockScheduler(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.coords = new ArrayList<>();
    }

    public void addRange(Range r) {
        this.coords.add(r);
    }

    public void run() {

    }



}
