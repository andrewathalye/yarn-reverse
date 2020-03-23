import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.TinyUtils;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
class Map {
	public static void main(String[] args) {
		File input, output, mappings, libraries;
		input=new File(args[0]);		
		output=new File(args[1]);
		mappings=new File(args[2]);
		libraries=new File(args[3]);
		String from, to;
		from=args[4];
		to=args[5];
		if (output.exists()) {
			output.delete();
		}

		TinyRemapper remapper = TinyRemapper.newRemapper()
				.withMappings(TinyUtils.createTinyMappingProvider(mappings.toPath(), from, to))
				.renameInvalidLocals(true)
				.rebuildSourceFilenames(true)
				.build();

		try {
			OutputConsumerPath outputConsumer = new OutputConsumerPath(output.toPath());
			outputConsumer.addNonClassFiles(input.toPath());
			remapper.readInputs(input.toPath());

			Iterator it = FileUtils.iterateFiles(libraries, null, false);
			while (it.hasNext()){
				remapper.readClassPath(((File)it.next()).toPath());			
			}
			remapper.apply(outputConsumer);
			outputConsumer.close();
			remapper.finish();
		} catch (Exception e) {
			remapper.finish();
			throw new RuntimeException("Failed to remap jar", e);
		}
	}
}
