package de.bkroeger.editor4;

import java.util.ArrayList;

public class CommandOptions
{
    protected ArrayList<String> arguments = new ArrayList<>();
    
    public CommandOptions(String[] args) {
        parse(args);
    }
    
    public void parse(String[] args) {
        arguments.clear();
        for ( int i = 0; i < args.length; i++ ) {
            arguments.add(args[i]);
        }
    }
    
    public int size() {
        return arguments.size();
    }
    
    public boolean hasOption(String option) {
        boolean hasValue = false;
        String str;
        for ( int i = 0; i < arguments.size(); i++ ) {
            str = (String)arguments.get(i);
            if ( str.equalsIgnoreCase(option) ) {
                hasValue = true;
                break;
            }
        }
        return hasValue;
    }
    
    public String valueOf(String option) {
        String value = null;
        String str;
        for ( int i = 0; i < arguments.size(); i++ ) {
            str = (String)arguments.get(i);
            if ( str.equalsIgnoreCase(option) ) {
                value = (String)arguments.get(i+1);
                break;
            }
        }
        return value;
    }

}