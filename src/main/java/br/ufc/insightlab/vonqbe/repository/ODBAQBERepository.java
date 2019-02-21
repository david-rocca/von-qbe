package br.ufc.insightlab.vonqbe.repository;


import java.util.LinkedList;
import java.util.List;

import br.ufc.insightlab.vonqbe.exception.ErrorFileMessage;
import br.ufc.insightlab.vonqbe.service.impl.RORServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufc.insightlab.ror.entities.ResultQuery;
import br.ufc.insightlab.vonqbe.entity.WebResultItem;
import br.ufc.insightlab.vonqbe.service.QBEService;
import br.ufc.insightlab.vonqbe.service.RORService;
import br.ufc.insightlab.vonqbe.service.impl.DummyRORServiceImpl;
import br.ufc.insightlab.vonqbe.service.impl.QBEServiceImpl;

public class ODBAQBERepository extends QBERepository {

    QBEService qbeService;
    RORService rorService;

    Logger logger = LoggerFactory.getLogger(ODBAQBERepository.class);

    public ODBAQBERepository(String name, String mappingPath, String owlPath, String ntPath){
        qbeService = new QBEServiceImpl(ntPath);

        try {
         rorService = new RORServiceImpl(mappingPath, owlPath);
        } catch (Exception ex) {
         throw new ErrorFileMessage(ex.getCause().getMessage());
        }
       //rorService = new DummyRORServiceImpl();
        insertRepository(name, this);
    }

    public static QBERepository createODBAQBERepository(String name, String mappingPath, String owlPath, String ntPath) {
        return new ODBAQBERepository(name, mappingPath, owlPath, ntPath);
    }

    public List<String> helper(String text){
        return qbeService.helper(text);
    }

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

    //public ResultQuerySet applyQuery(String sparql){
    public Iterable<Object> applyQuery(String sparql){
        try {
            return (Iterable<Object>) rorService.run(sparql).iterator();
        } catch (Exception e) {
            logger.error(e.toString());
            //return new ResultQuerySet(null,null);
            return null;
        }
    }

    //public List<WebResultItem> mapResults(ResultQuerySet results){
    public List<WebResultItem> mapResults(Iterable<Object> results){
        List<WebResultItem> resultsList = new LinkedList<>();

        while(results.iterator().hasNext()){
            ResultQuery r = (ResultQuery) results.iterator().next();
            resultsList.add(new WebResultItem(r));
        }

        return resultsList;
    }

    public List<WebResultItem> runQuery(String text, int limit){
        return mapResults(applyQuery(getSPARQL(text, limit)));
    }

}