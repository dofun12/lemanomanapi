package org.lemanoman.interpreters;

public class ArgsInterpreter {
    String[] args;
    ArgsListener listener;

    public ArgsInterpreter(String[] args, ArgsListener listener) {
        this.args = args;
        this.listener = listener;

        if (listener != null) {
            if (args != null) {
                for (String arg : args) {
                    arg = arg.replaceAll("\"","");
                    if (arg.startsWith("--")) {
                        if (arg.equals("--help")) {
                            listener.onHelp();
                            return;
                        } else {
                            listener.onOperation(arg.substring(2));
                        }
                    } else {
                        if(arg.contains("=") && arg.split("=").length>1){
                            String key = arg.split("=")[0];
                            String value = arg.split("=")[1];
                            listener.onSetParameter(key, value);
                        }else{
                            System.err.println("Invalid parameter "+arg);
                            System.err.println(" to set parameters use: param=value");
                        }

                    }

                }
            }

        }
    }


    public static void main(String... args) {
        args = new String[]{
                "config.path=teste", "--teste"
        };

        new ArgsInterpreter(args, new ArgsListener() {
            @Override
            public void onHelp() {
                System.out.println("Showing help");
            }

            @Override
            public void onSetParameter(String key, String value) {
                System.out.println("Key: " + key + " ; Value: " + value);
            }

            @Override
            public void onOperation(String operation) {
                System.out.println("Running operation: " + operation);
            }
        });


    }

}
