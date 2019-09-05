package android.support.v7.widget;

import java.lang.reflect.Field;

import androidx.recyclerview.widget.RecyclerView;


public abstract class ViewHolderDelegate {

    private ViewHolderDelegate() {
        throw new UnsupportedOperationException("no instances");
    }

    public static void setPosition(RecyclerView.ViewHolder viewHolder, int position) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("androidx.recyclerview.widget.RecyclerView.ViewHolder");

            Field field = clazz.getDeclaredField("mPosition");
            field.setAccessible(true);
            field.set(viewHolder, position);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
