package com.worktimetracker.DataClasses;

public record Pair<K, V>(K first, V second) {
    @Override
    public String toString(){
        return "<"+first.toString()+", "+second.toString()+">";
    }
}
