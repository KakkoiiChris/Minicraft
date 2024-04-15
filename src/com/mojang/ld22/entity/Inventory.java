package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public List<Item> items = new ArrayList<>();

    public void add(Item item) {
        add(items.size(), item);
    }

    public void add(int slot, Item item) {
        if (item instanceof ResourceItem toTake) {
            ResourceItem has = findResource(toTake.resource);
            if (has == null) {
                items.add(slot, toTake);
            } else {
                has.count += toTake.count;
            }
        } else {
            items.add(slot, item);
        }
    }

    private ResourceItem findResource(Resource resource) {
        for (Item item : items) {
            if (item instanceof ResourceItem has) {
                if (has.resource == resource) return has;
            }
        }
        return null;
    }

    public boolean hasResources(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        return ri.count >= count;
    }

    public void removeResource(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return;
        if (ri.count < count) return;
        ri.count -= count;
        if (ri.count <= 0) items.remove(ri);
    }

    public int count(Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = findResource(((ResourceItem) item).resource);
            if (ri != null) return ri.count;
        } else {
            int count = 0;
            for (Item value : items) {
                if (value.matches(item)) count++;
            }
            return count;
        }
        return 0;
    }
}
