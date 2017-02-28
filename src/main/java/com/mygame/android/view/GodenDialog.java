package com.mygame.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mygame.android.library.R;
 
/**
 * 
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * @author antoine vianey
 *
 */
public class GodenDialog extends Dialog {
 
    public GodenDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public GodenDialog(Context context) {
        super(context);
    }
 
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
 
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String neutralButtonText;
        private String negativeButtonText;
        private View contentView;
        private String[] selectItems;
        private DialogInterface.OnClickListener 
                        positiveButtonClickListener,
                        negativeButtonClickListener,
                        neutralButtonClickListener,
                        listItemClickListener
                        ;
        private int buttoncount;
        public Builder(Context context) {
            this.context = context;
        }
 
        /**
         * Set the Dialog message from String
         * @param title
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
 
        /**
         * Set the Dialog message from resource
         * @param title
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }
 
        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }
 
        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
 
        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
 
        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            buttoncount ++;
            return this;
        }
 
        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener) {
        	buttoncount ++;
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener) {
        	buttoncount ++;
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }
        
        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener) {
        	buttoncount ++;
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */

        public Builder setNeutralButton(String neutralButtonText,
                DialogInterface.OnClickListener listener) {
        	buttoncount ++;
            this.neutralButtonText = neutralButtonText;
            this.neutralButtonClickListener = listener;
            return this;
        }
        
        public Builder setNeutralButton(int neutralButtonText,
                DialogInterface.OnClickListener listener) {
        	buttoncount ++;
            this.neutralButtonText = (String) context
                    .getText(neutralButtonText);;
            this.neutralButtonClickListener = listener;
            return this;
        }
        
        public Builder setItems(String[] selectItems,DialogInterface.OnClickListener listener){
        	buttoncount ++;
        	this.selectItems = selectItems;
        	this.listItemClickListener = listener;
        	return this;
        }
        
        /**
         * Create the custom dialog
         */
        public GodenDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final GodenDialog dialog = new GodenDialog(context, 
            		R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog, null);
            
            int width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
            LayoutParams layoutParams = new LinearLayout.LayoutParams(
            		width / 4 * 3, LayoutParams.WRAP_CONTENT);
            // set the dialog title
            if(title == null){
            	layout.findViewById(R.id.header).setVisibility(View.GONE);
            }else{
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	if (positiveButtonClickListener != null) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                	}else{
                                		dialog.dismiss();
                                	}
                                }
                            });
                
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	if (negativeButtonClickListener != null) {
                                		negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                	}else{
                                		dialog.dismiss();
                                	}
                                }
                            });
                
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            
            if (neutralButtonText != null) {
                ((Button) layout.findViewById(R.id.neutralButton))
                        .setText(neutralButtonText);
                    ((Button) layout.findViewById(R.id.neutralButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	if (neutralButtonClickListener != null) {
                                		neutralButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEUTRAL);
                                	}else{
                                		dialog.dismiss();
                                	}
                                }
                            });
                
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.neutralButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(
                		R.id.message)).setText(message);
            }else if(null != selectItems){
            	((TextView) layout.findViewById(
                		R.id.message)).setVisibility(View.GONE);
            	ListView select_list = (ListView)layout.findViewById(
                		R.id.select_list);
            	//select_list.setLayoutParams(layoutParams);
            	select_list.setVisibility(View.VISIBLE);
            	select_list.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(listItemClickListener == null){
							dialog.dismiss();
						}else{
							listItemClickListener.onClick(dialog, arg2);
						}
					}
				});
            	DialogSelectItemAdapter adapter_ = dialog.new DialogSelectItemAdapter(selectItems);
            	select_list.setAdapter(adapter_);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT, 
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.addContentView(layout, new LayoutParams(width / 4 * 3,LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }
 
    }
 
    class DialogSelectItemAdapter extends BaseAdapter{
    	String[] selectItems;
		public DialogSelectItemAdapter(String[] selectItems) {
			super();
			this.selectItems = selectItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return selectItems == null ? 0 : selectItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return selectItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Holder holder = null;
			if(null == arg1){
				holder = new Holder();
				arg1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_item, null);
				holder.selectItem = (TextView)arg1.findViewById(R.id.dialog_select_item_text);
				arg1.setTag(holder);
			}else{
				holder = (Holder)arg1.getTag();
			}
			holder.selectItem.setText(selectItems[arg0]);
			return arg1;
		}
    	
		class Holder {
			TextView selectItem;
		}
    }
}
