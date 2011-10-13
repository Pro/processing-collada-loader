package colladaLoader.xmlMapping;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p>Description: some xml-Subtags have common behavior. Such Helper-Classes implement the following Methods</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
abstract class SubTag {

/**
 * links to the next sub-tag (if exists)to get more informations. Otherwise it returns the ID
 * @return the next Tag-ID or source, whatever
 */
abstract String getNextSource();
/**
 * returns the ID of this xml-tag
 * @return the tag-ID
 */
abstract String getID();

}
