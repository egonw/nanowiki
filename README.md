# NanoWiki

## Backing up the MediaWiki database

```shell
mysqldump -u root -p wikidb --default-character-set=latin1 > backup.sql.txt
sudo bash
php /usr/share/mediawiki/maintenance/dumpBackup.php --conf /etc/mediawiki/LocalSettings.php --aconf /etc/mediawiki/AdminSettings.php --full > backup.xml.txt
export MW_INSTALL_PATH=/usr/share/mediawiki
php /usr/local/share/mediawiki-extensions/SemanticMediaWiki/maintenance/SMW_dumpRDF.php --conf /etc/mediawiki/LocalSettings.php --aconf /etc/mediawiki/AdminSettings.php > backup.rdf.txt
```
