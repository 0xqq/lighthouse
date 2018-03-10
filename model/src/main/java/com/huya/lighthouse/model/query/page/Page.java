package com.huya.lighthouse.model.query.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;

/**
 * 包含分页信息 及 List的分页对象
 * 
 * 
 * @param <T>
 */
public class Page<T> implements Iterable<T>, java.io.Serializable {

	private static final long serialVersionUID = 478326676132962632L;
	
	private List<T> itemList = new ArrayList<T>(0);
	private Paginator paginator = new Paginator(0, 0, 0);

	public Page() {
	}

	public Page(Paginator paginator) {
		setPaginator(paginator);
	}

	public Page(List<T> itemList, Paginator paginator) {
		setItemList(itemList);
		setPaginator(paginator);
	}

	public List<T> getItemList() {
		return itemList;
	}

	public void setItemList(List<T> itemList) {
		if (itemList == null)
			throw new IllegalArgumentException("'itemList' must be not null");
		this.itemList = itemList;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		if (paginator == null)
			throw new IllegalArgumentException("'paginator' must be not null");
		this.paginator = paginator;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return itemList == null ? IteratorUtils.EMPTY_ITERATOR : itemList.iterator();
	}

}
