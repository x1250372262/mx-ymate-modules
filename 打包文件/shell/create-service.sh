#!/bin/bash
JAR_NAME="sft-tej-manager-1.0.0.war";
JAR_PATH="/usr/local/java/servers/sft-tej-manager/"
JAR_PATT_FULLY="$JAR_PATH$JAR_NAME";
FILE_SERVICE="sft-tej-manager.service";
DIR_SERVICE="/etc/systemd/system/"$FILE_SERVICE;

echo $DIR_SERVICE
if [ ! -f "$DIR_SERVICE" ];then
	touch $DIR_SERVICE
	echo "创建服务文件成功:"$DIR_SERVICE
else
	rm -r $DIR_SERVICE
	touch $DIR_SERVICE
	echo "服务文件已删除重建成功:"$DIR_SERVICE
fi

echo "文件写入中:"$DIR_SERVICE
echo "[Unit]" >> $DIR_SERVICE
echo "Description= $FILE_SERVICE" >> $DIR_SERVICE
echo "After=syslog.target" >> $DIR_SERVICE

echo "[Service]" >> $DIR_SERVICE
echo "ExecStart=/usr/bin/java -jar $JAR_PATT_FULLY" >> $DIR_SERVICE
echo "SuccessExitStatus=143" >> $DIR_SERVICE
echo "WorkingDirectory=$JAR_PATH" >> $DIR_SERVICE

echo "[Install]" >> $DIR_SERVICE
echo "WantedBy=multi-user.target" >> $DIR_SERVICE


echo "文件写入完成"

echo "服务创建完成:"FILE_SERVICE