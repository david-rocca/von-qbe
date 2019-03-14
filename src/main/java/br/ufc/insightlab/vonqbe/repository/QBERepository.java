package br.ufc.insightlab.vonqbe.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.insightlab.vonqbe.exception.ErrorFileMessage;
import br.ufc.insightlab.vonqbe.service.impl.RORServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufc.insightlab.ror.entities.ResultQuery;
import br.ufc.insightlab.ror.entities.ResultQuerySet;
import br.ufc.insightlab.vonqbe.entity.WebResultItem;
import br.ufc.insightlab.vonqbe.service.QBEService;
import br.ufc.insightlab.vonqbe.service.RORService;
import br.ufc.insightlab.vonqbe.service.impl.DummyRORServiceImpl;
import br.ufc.insightlab.vonqbe.service.impl.QBEServiceImpl;

public abstract class QBERepository {

    private static Logger logger = LoggerFactory.getLogger(QBERepository.class);

    private static Map<String, QBERepository> containers;

    private QBEService qbeService;

    // public QBEService getQBEService(){
    //     return this.qbeService;
    // }

    // public RORService getRORService(){
    //     return this.rorService;
    // }


    public QBERepository(String ntPath) {
        this.qbeService = new QBEServiceImpl(ntPath);
    }

    public static Set<String> getDatabases(){

        if(containers == null)
            init();

        return containers.keySet();
    }

    public static void init(){
        containers = new HashMap<>();

    }

    public void insertRepository(String name, QBERepository repository){
        if(containers == null)
            init();

        containers.put(name, this);
    }

    public static QBERepository getRepository(String name){
        return containers.get(name);
    }

    public static boolean containsControler(String name){
        return containers.containsKey(name);
    }

    public abstract List<WebResultItem> runQuery(String textDecoder, int limit) throws Exception;
    
    public String getSPARQL(String text, int limit){
        try{
            if (limit <= 0) {
                return qbeService.query(text);
            }else {
                return qbeService.query(text)+"LIMIT " + limit;
            }
        }
        catch(Exception e){
            return "";
        }
    }

    //public abstract ResultQuerySet applyQuery(String sparql);
    public abstract Iterable<Object> applyQuery(String sparql) throws Exception;

    public abstract List<WebResultItem> mapResults(Iterable<Object> resultSet);

    public List<String> helper(String text){
        return qbeService.helper(text);
    }
}