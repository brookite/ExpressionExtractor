package com.github.brookite.extractexpr;

import org.apache.jena.rdf.model.Model;
import org.vstu.meaningtree.serializers.rdf.RDFSerializer;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class ASTSerializer {
    public static void meaningTreeTtl(SourceCodeParser.Node node, LanguageInfo info, OutputStream stream) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        RDFSerializer rdfSerializer = new RDFSerializer();
        Model model = rdfSerializer.serialize(info.createExpressionMeaningTree(node).getRootNode());
        model.write(stream, "TTL");
    }
}
