#!/usr/bin/env sh

cd `dirname $0`

STE_HOME=`pwd`;

JARPATH=$STE_HOME:/$STE_HOME/*:$STE_HOME/lib/*
JAVA=java

Usage() {
    echo -e "   Usage: $(basename $0)
    crawl)         	     start to crawl a props, usage: ./ste.sh crawl xxxx.props;;
    crawling)         	     check crawling props, usage: ./ste.sh crawling;;
    stop)                    stop crawl a props;;
        "
}

p1=$1;
p2=$2;
p3=$3;
p4=$4;
p5=$5;

crawl() {
    running=`ps auxwww | grep java | grep SteMain | grep $p2 | wc -l`;
    if [ $running -ge 1 ]
    then
        echo "$p2 is crawling, ignore start crawl $p2";
    else
        EXEC com.souche.ste.SteMain;
    fi
}

crawling() {
    ps auxwww | grep SteMain | grep -v grep | grep -v sed | sed "s/.*SteMain //";
}

stopCrawl() {
    running=`ps auxwww | grep java | grep SteMain | grep $p2 | wc -l`;
    if [ $running -lt 1 ]
    then
        echo "$p2 is not crawling, ignore stop $p2";
    else
        ps auxwww | grep java | grep SteMain | grep $p2 | awk '{print $2}' | xargs kill -9
    fi
}

EXEC() {
    echo $JAVA -Xms200m -Xmx500m -cp $JARPATH $1 $p2 $p3 $p4
    $JAVA  -Xms200m -Xmx500m -XX:+UseParallelOldGC -XX:+PrintGCDetails -Xloggc:gc.log -cp $JARPATH $1 $p2 $p3 $p4
}


case "$p1" in 
    "")                      Usage            ;;
    "-h")                    Usage            ;;
    "-help")                 Usage            ;;
    "--help")                Usage            ;;
    "help")                  Usage            ;;
    crawl)         	     crawl;;
    crawling)         	     crawling;;
    stop)                    stopCrawl;;
    monitor)                    EXEC com.souche.ste.props.PropsMain;;
esac

