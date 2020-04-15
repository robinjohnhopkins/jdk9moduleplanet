package com.process.api;

public class KillOtherProcess {
    public static void main(String... args) throws Exception {
        String searchTerm = "";
        if (args.length > 0) {
            searchTerm = args[0];
            System.out.println("searching process to kill containing " + searchTerm);
            findAndKillProcess(searchTerm);
        } else {
            System.out.println("usage: java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/process/api/KillOtherProcess <term in cmd to kill>");
        }
    }
    public static void findAndKillProcess(final String searchTerm) throws Exception {

        ProcessHandle textEditHandle =
                ProcessHandle.allProcesses()
                        .filter(h -> h.info().commandLine().map(cmd -> cmd.contains(searchTerm) && !cmd.contains("KillOtherProcess")).orElse(false))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No matching handle found"));

        System.out.println(textEditHandle.info());

        textEditHandle.onExit()
                .thenAccept(h -> System.out.println(searchTerm + " was killed by Java!"));

        boolean shutdown = textEditHandle.destroy();

        textEditHandle.onExit().join();
        System.out.println("Shutdown: " + shutdown);

    }

}

