package ar.com.osde.bot;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Command for the bots
 * @date 20 of June of 2015
 */
public class Commands {
    public static final String commandInitChar = "/";
    /// Transifex iOS command

    public static final String help = commandInitChar + "help";
    /// Upload command
    public static final String uploadCommand = commandInitChar + "upload";
    /// Start command
    public static final String startCommand = commandInitChar + "start";
    /// Cancel command
    public static final String cancelCommand = commandInitChar + "cancel";
    /// Delete command
    public static final String deleteCommand = commandInitChar + "delete";
    /// List command
    public static final String listCommand = commandInitChar + "list";
    /// Set Language command
    public static final String setLanguageCommand = commandInitChar + "language";

    public static final String STOPCOMMAND = commandInitChar + "stop";
}
