package com.github.brookite.extractexpr;

import org.apache.jena.rdf.model.Model;
import org.vstu.meaningtree.serializers.rdf.RDFSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class ASTSerializer {
    public static boolean meaningTreeTtl(SourceCodeParser.Node node, LanguageInfo info, SourceCodeRepositoryInfo repo, OutputStream stream) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        if (repo != null) {
            stream.write(String.format("# REPO_NAME %s\n", repo.name).getBytes(StandardCharsets.UTF_8));
            stream.write(String.format("# LICENSE %s\n", repo.license).getBytes(StandardCharsets.UTF_8));
            stream.write(String.format("# REPO_URL %s\n", repo.url).getBytes(StandardCharsets.UTF_8));
        }

        var result = info.createExpressionMeaningTree(node);
        if (result != null) {
            RDFSerializer rdfSerializer = new RDFSerializer();
            Model model = rdfSerializer.serialize(result);
            model.write(stream, "TTL");
            return true;
        } else {
            return false;
        }
    }
}
