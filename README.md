Scala SALAD
===========

SALAD: 
Slick
Argonaut
Lift
Angular
Derby

The project is meant to be a step-by-step-no-steps-missed walkthrough to create a generic but useful setup for multi-tier web-based Scala application, using  Derby as a database backend, Slick as an FRM toolkit and Argonaut as a Json processor. 

# Scala SALAD

## Define `SALAD_HOME`

1. Create SALAD directory
```bash
mkdir ~/SALAD
```
1. Define SALAD_HOME environment variable
```bash
export SALAD_HOME=~/SALAD
```
1. Create $SALAD/saladenv.sh file that will contain environment configuration.
Add previous command to it.
1. Create `_downloads` directory
```bash
mkdir $SALAD_HOME/_downloads
```

## Install Jetty

1. Download .zip file from http://download.eclipse.org/jetty/ into 	`$SALAD_HOME/_downloads`.
1. Unzip its contents into $SALAD_HOME
	```bash
	unzip $SALAD_HOME/_downloads/jetty-distribution-9.3.5.v20151012.zip -d $SALAD_HOME
	```
1. Export JETTY_HOME environment variable
	```bash
	export JETTY_HOME=$SALAD_HOME/jetty-distribution-9.3.3.v20151-12
	```
1. Append `JETTY_HOME/bin` to the `PATH` variable
	```bash
    PATH=$PATH:$JETTY_HOME/bin
	```
**Note:** Add JETTY_HOME definition to the `saladenv.sh` file
## Install Derby DB

**Note:** Open separate Terminal window for Jetty and Derby DB so you can leave their instances running in it.

1. Download latest Derby DB distribution `db-derby-*-bin.zip` file (i.e., `10.12.1.1`) from http://db.apache.org/derby/releases/ .cgi link into `$SALAD_HOME/_downloads`
1. Unzip its contents into `$SALAD_HOME`
	```bash
    unzip $SALAD_HOME/_downloads/db-derby-10.12.1.1-bin.zip -d $SALAD_HOME
    ```
1. Export DERBY_HOME environment variable
	```bash
	export DERBY_HOME=$SALAD_HOME/db-derby-10.12.1.1-bin
	```
1. Append `$DERBY_HOME/bin` to the `PATH` variable
	```bash
    PATH=$PATH:$DERBY_HOME/bin
	```

## Test Derby DB Installation

**Note:** Add DERBY_HOME, DERBY_OPTS, and DERBY_HOME/bin definitions to the `saladenv.sh` file

1. Create a directory for database files
	```bash
	mkdir $SALAD_HOME/db-derby-home
    ```
1. Creave Derby DB configuration with db-derby-home directory, assuming default default port `1527`
	```bash
    export DERBY_OPTS=-Dderby.system.home=$SALAD_HOME/db-derby-home 
    ```

1. Start the database in the server mode
	```bash
    startNetworkServer
    ```
    Upon successful start you will see diagnostic output
    ```
    startNetworkServer
	Wed Oct 21 23:26:39 BST 2015 : Security manager installed using the Basic server security policy.
	Wed Oct 21 23:26:40 BST 2015 : Apache Derby Network Server - 10.12.1.1 - (1704137) started and ready to accept connections on port 1527
    ```
1. Open new Terminal window. Source the environment
	```bash
    source ~/SALAD/saladenv.sh
    ```
1. Launch derby interative scripting tool utility ij
	```bash
    ij
    ```
1. Type in `connect` ij command with ;create=true prefix to create test-db database
	```bash
    connect 'jdbc:derby://localhost:1527/test-db;create=true';
    ```
	The database should be created in $SALAD_HOME/db-derby-home directory
    ```
    $ ls -ls $SALAD_HOME/db-derby-home 
	total 8
	8 -rw-r--r--  1 nz  staff  187 21 Oct 23:29 derby.log
	0 drwxr-xr-x  9 nz  staff  306 21 Oct 23:28 test-db
    ```
1. Output contents of `sys.systables` table
	```
    ij> select tablename from sys.systables;
	TABLENAME                  
    ---------------------------
	SYSALIASES                                       
	SYSCHECKS                                         
	SYSCOLPERMS                                       
	SYSCOLUMNS                 
	...
	SYSTABLES                                         
	SYSTRIGGERS                                       
	SYSUSERS                                         
	SYSVIEWS                                         

	23 rows selected
	ij>
    ```
1. Enter `exit;` to quit ij.
